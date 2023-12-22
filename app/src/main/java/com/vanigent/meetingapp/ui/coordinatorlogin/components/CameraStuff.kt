package com.vanigent.meetingapp.ui.coordinatorlogin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Photo
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.vanigent.meetingapp.ui.coordinatorlogin.components.PhotoBottomSheetContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
//private var isPhotoCaptured = false // Add this flag at the beginning of the file

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraStuff(
    extractedText: MutableMap<String, String>,
    onReceiptDetailsUpdated: (MutableMap<String, String>, Bitmap) -> Unit,
    closeCameraPreview: (Bitmap?) -> Unit
) {
    Timber.e("Camera stuff")
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE or
                        CameraController.VIDEO_CAPTURE
            )
        }
    }

    // TextRecognitionCallback implementation
//    val textRecognitionCallback = remember {
//        object : TextRecognitionCallback {
//
//
//
//        }
//    }

    val textRecognizer = remember {
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }

//    val textAnalyzer = remember {
//        TextAnalyzer(textRecognizer = textRecognizer, callback = textRecognitionCallback)
//    }

    // Create a new ImageAnalysis instance
//    val imageAnalysis = remember {
//        ImageAnalysis.Builder()
//            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//            .build()
//    }

    // Set the ImageAnalysis analyzer
//    imageAnalysis.setAnalyzer(Dispatchers.Main, textAnalyzer::analyze)


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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                IconButton(
                    onClick = {
                        scope.launch {
                            scaffoldState.bottomSheetState.expand()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Photo,
                        contentDescription = "Open gallery"
                    )
                }
                IconButton(
                    onClick = {
                        takePhoto(
                            controller = controller,
                            textRecognizer = textRecognizer,
                            extractedText = extractedText,
                            context = context,
                            closeCameraPreview = closeCameraPreview,
                            onReceiptDetailsUpdated = onReceiptDetailsUpdated,
//                            textRecognitionCallback = textRecognitionCallback
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
}

private fun takePhoto(
    controller: LifecycleCameraController,
    textRecognizer: TextRecognizer,
    extractedText: MutableMap<String, String>,
    context: Context,
    closeCameraPreview: (Bitmap?) -> Unit,
    onReceiptDetailsUpdated: (MutableMap<String, String>, Bitmap) -> Unit,
//    textRecognitionCallback: TextRecognitionCallback
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
//                        extractedText = extractedText
                        callback = object : TextRecognitionCallback {
                            override fun onSuccess(visionText: Text) {
//                                if (isPhotoCaptured) {
//                                    Timber.e("Photo has already been captured. Ignoring.")
//                                    return
//                                }
//                                Timber.e("Photo hasn't been captured. Continuing.")
//                                isPhotoCaptured = true // Set the flag to true to prevent further calls

                                // Handle successful recognition
                                // Extracted text is available here
                                Timber.e("extractedText cameraStuff")
//                                extractedText.clear()
                                visionText.let { res ->

                                    if (res.text.isBlank()) {
                                        Timber.e("Could not read receipt")
                                    } else {
                                        val firstBlock = res.textBlocks.firstOrNull()?.text.toString()
                                        val vendorName = validateVendorName(firstBlock)
                                        Timber.d("vendorName - $vendorName")
                                        extractedText["VENDOR NAME"] = vendorName

                                        for (block in res.textBlocks) {
                                            val blockText = block.text

                                            Timber.d("block - $blockText")

                                            val potentialAmountLabels = listOf("TOTAL", "AMOUNT")


                                            // Check for specific keywords
                                            if (blockText.contains("TOTAL", ignoreCase = true)) {
                                                // Extract subtotal from the current block
                                                extractSubtotalFromBlock(block)

                                                // Look for nearby lines for subtotal
                                                val nearbyLines = findNearbyLines(res, block)
                                                for ((index, line) in nearbyLines.withIndex()) {
                                                    // Extract every second value from the nearby lines
                                                    if (index % 2 == 1) {
                                                        val subtotalFromLine = extractSubtotalFromLine(line)
                                                        extractedText["TOTAL"] = subtotalFromLine
                                                    }
                                                }
                                            } else if (blockText.contains("ADDRESS", ignoreCase = true)) {
                                                extractSubtotalFromBlock(block)

                                                // Look for nearby lines for subtotal
                                                val nearbyLines = findNearbyLines(res, block)
                                                for ((index, line) in nearbyLines.withIndex()) {
                                                    // Extract every second value from the nearby lines
                                                    if (index % 2 == 1) {
                                                        val subtotalFromLine = extractSubtotalFromLine(line)
                                                        extractedText["ADDRESS"] = subtotalFromLine
                                                    }
                                                }
                                            } else if (blockText.contains("CASH", ignoreCase = true)) {
                                                // Extract subtotal from the current block
                                                extractSubtotalFromBlock(block)

                                                // Look for nearby lines for subtotal
                                                val nearbyLines = findNearbyLines(res, block)
                                                for ((index, line) in nearbyLines.withIndex()) {
                                                    // Extract every second value from the nearby lines
                                                    if (index % 2 == 1) {
                                                        val subtotalFromLine = extractSubtotalFromLine(line)
                                                        extractedText["CASH"] = subtotalFromLine
                                                    }
                                                }
                                            }
                                            Timber.d("blockText - $blockText")
                                        }
                                    }

                                    Timber.e("extractedText.isEmpty() - ${extractedText.isEmpty()}")
                                    extractedText.map { text ->
                                        Timber.e("\n ${text.key} - ${text.value}")
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

//                    Timber.e("extractedText cameraStuff")
//                    extractedText.map {text ->
//                        Timber.e("\n ${text.key} - ${text.value}")
//                    }
//                    onReceiptDetailsUpdated(extractedText, rotatedBitmap)
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

@Composable
fun FullScreenCameraPreview(
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

                if ((lineBoundingText.bottom >= targetBoundingText.top)
                    && (lineBoundingText.top <= targetBoundingText.bottom)
                ) {
                    nearbyLines.add(line)
                }
            }
        }
    }

    return nearbyLines
}

private fun extractSubtotalFromBlock(block: Text.TextBlock) {
    // Implement logic to extract subtotal information from the block
    val subtotalText = block.text
    Timber.d("Found Subtotal in Block: $subtotalText")
    // Add your logic to extract and handle the subtotal value
}

private fun extractSubtotalFromLine(line: Text.Line): String {
    // Implement logic to extract subtotal information from the line
    val subtotalText = line.text
    Timber.d("Found Subtotal in Line: $subtotalText")
    // Add your logic to extract and handle the subtotal value
    return subtotalText
}

private fun extractTaxExclusiveTotal(block: Text.TextBlock) {
    // Implement logic to extract tax-exclusive total information from the block
    val taxExclusiveTotalText = block.text
    Timber.d("Found Tax Exclusive Total: $taxExclusiveTotalText")
    // Add your logic to extract and handle the tax-exclusive total value
}

private fun validateVendorName(companyName: String?): String {
    return if (!companyName.isNullOrBlank()) {
        companyName.replace("RECEIPT", "", true)
    } else
        ""
}