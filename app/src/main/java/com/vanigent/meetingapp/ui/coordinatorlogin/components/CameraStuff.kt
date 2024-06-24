package com.vanigent.meetingapp.ui.coordinatorlogin.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.vanigent.meetingapp.ui.coordinatorlogin.TextAnalyzer
import com.vanigent.meetingapp.ui.coordinatorlogin.TextRecognitionCallback
import timber.log.Timber

private const val LINE_VERTICAL_THRESHOLD = 10

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraStuff(
    extractedText: MutableMap<String, String>,
    onReceiptDetailsUpdated: (MutableMap<String, String>, Bitmap) -> Unit,
    closeCameraPreview: (Bitmap?) -> Unit
) {
    Timber.e("Camera stuff")
    val context = LocalContext.current
    val scaffoldState = rememberBottomSheetScaffoldState()
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE or
                        CameraController.VIDEO_CAPTURE
            )
        }
    }

    val textRecognizer = remember {
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            PhotoBottomSheetContent(
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            CameraPreview(
                controller = controller,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            )

            IconButton(
                onClick = {
                    controller.cameraSelector =
                        if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                            CameraSelector.DEFAULT_FRONT_CAMERA
                        } else CameraSelector.DEFAULT_BACK_CAMERA
                },
                modifier = Modifier
                    .offset(16.dp, 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Cameraswitch,
                    contentDescription = "Switch camera"
                )
            }

            IconButton(
                modifier = Modifier.align(Alignment.BottomCenter),
                onClick = {
                    takePhoto(
                        controller = controller,
                        textRecognizer = textRecognizer,
                        extractedText = extractedText,
                        context = context,
                        closeCameraPreview = closeCameraPreview,
                        onReceiptDetailsUpdated = onReceiptDetailsUpdated
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = "Take photo"
                )
            }
        }

    }
}

private fun takePhoto(
    controller: LifecycleCameraController,
    textRecognizer: TextRecognizer,
    extractedText: MutableMap<String, String>,
    context: Context,
    closeCameraPreview: (Bitmap?) -> Unit,
    onReceiptDetailsUpdated: (MutableMap<String, String>, Bitmap) -> Unit,
) {

    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object : OnImageCapturedCallback() {
            @androidx.annotation.OptIn(ExperimentalGetImage::class)
            override fun onCaptureSuccess(imageProxy: ImageProxy) {
                super.onCaptureSuccess(imageProxy)

                val matrix = Matrix().apply {
                    postRotate(imageProxy.imageInfo.rotationDegrees.toFloat())
                }
                val rotatedBitmap = Bitmap.createBitmap(
                    imageProxy.toBitmap(),
                    0,
                    0,
                    imageProxy.width,
                    imageProxy.height,
                    matrix,
                    true
                )

                imageProxy.let {
                    TextAnalyzer(
                        textRecognizer = textRecognizer,
                        callback = object : TextRecognitionCallback {
                            override fun onSuccess(visionText: Text) {

                                Timber.e("extractedText cameraStuff")
                                visionText.let { res ->

                                    if (res.text.isBlank()) {
                                        Timber.e("Could not read receipt")
                                    } else {
                                        val firstBlock =
                                            res.textBlocks.firstOrNull()?.text.toString()
                                        val vendorName = validateVendorName(firstBlock)
                                        Timber.d("vendorName - $vendorName")
                                        extractedText["VENDOR NAME"] = vendorName

                                        for (block in res.textBlocks) {
                                            val blockText = block.text

                                            Timber.d("block - $blockText")

                                            if (blockText.equals("Sub Total", ignoreCase = true) ||
                                                blockText.equals("SubTotal", ignoreCase = true)) {
                                                continue // Skip processing for "Sub Total"
                                            }

                                            val potentialLabels = listOf("TOTAL", "CATERER", "DATE")

                                            for (label in potentialLabels) {

                                                if (blockText.contains(label, ignoreCase = true)) {
                                                    val nearbyLines = findNearbyLines(res, block)
                                                    for ((index, line) in nearbyLines.withIndex()) {
                                                        // Extract every second value from the nearby lines
                                                        if (index % 2 == 1) {
                                                            val subtotalFromLine =
                                                                extractTextFromLine(line)
                                                            extractedText[label] = subtotalFromLine
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    // Update extractedText here
                                    extractedText.putAll(extractedText)

                                    // Call onReceiptDetailsUpdated with rotatedBitmap
                                    onReceiptDetailsUpdated(extractedText, rotatedBitmap)

                                }
                            }

                            override fun onFailure(exception: Exception) {
                                // Handle recognition failure
                                Timber.e("visionText error - ${exception.message}")
                            }
                        }

                    ).analyze(it)
                }

                closeCameraPreview(rotatedBitmap)

            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Timber.e("Camera", "Couldn't take photo: ", exception)
            }
        }
    )
}

@Composable
fun CameraPreview(
    controller: LifecycleCameraController,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        factory = {
            PreviewView(it).apply {
                this.controller = controller
                controller.bindToLifecycle(lifecycleOwner)
            }
        },
        modifier = modifier
    )
}

private fun findNearbyLines(visionText: Text, targetBlock: Text.TextBlock): List<Text.Line> {
    val nearbyLines = mutableListOf<Text.Line>()

    val targetBoundingText = targetBlock.boundingBox

    for (block in visionText.textBlocks) {
        for (line in block.lines) {
            val lineBoundingText = line.boundingBox

            if (lineBoundingText != null && targetBoundingText != null) {
                if (isValueOnTheRight(line, targetBlock)) {
                    // For lines with value on the right, use the original condition
                    if ((lineBoundingText.bottom >= targetBoundingText.top)
                        && (lineBoundingText.top <= targetBoundingText.bottom)
                    ) {
                        nearbyLines.add(line)
                    }
                } else {
                    // For lines with value below, use the condition with the threshold
                    if ((lineBoundingText.bottom >= targetBoundingText.top)
                        && (lineBoundingText.top <= targetBoundingText.bottom + LINE_VERTICAL_THRESHOLD)
                    ) {
                        nearbyLines.add(line)
                    }
                }
            }
        }
    }

    return nearbyLines
}

private fun extractTextFromLine(line: Text.Line): String {
    // Implement logic to extract subtotal information from the line
    val subtotalText = line.text
    Timber.d("Found Subtotal in Line: $subtotalText")
    // Add your logic to extract and handle the subtotal value
    return subtotalText
}

private fun validateVendorName(companyName: String?): String {
    return if (!companyName.isNullOrBlank()) {
        companyName.replace("RECEIPT", "", true)
    } else
        ""
}

// Function to check if the value is on the right of the label
private fun isValueOnTheRight(line: Text.Line, labelBlock: Text.TextBlock): Boolean {
    // Implement logic to check if the value is on the right of the label
    // You may need to analyze the relative positions of the line and label bounding boxes
    // and return true if the value is on the right, false otherwise.
    // For simplicity, this function assumes that if the line's left is to the right of the label's right,
    // then the value is on the right.
    return (line.boundingBox?.left ?: 0) > (labelBlock.boundingBox?.right ?: 0)
}