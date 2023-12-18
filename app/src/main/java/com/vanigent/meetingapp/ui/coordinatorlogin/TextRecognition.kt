package com.vanigent.meetingapp.ui.coordinatorlogin

import android.annotation.SuppressLint
import android.content.Context
import android.media.Image
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import timber.log.Timber
import java.util.concurrent.Executors

@Composable
fun MLKitTextRecognition() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val extractedText = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TextRecognitionView(
            context = context,
            lifecycleOwner = lifecycleOwner,
            extractedText = extractedText
        )
        Text(
            text = extractedText.value,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        )
    }
}

@Composable
fun TextRecognitionView(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    extractedText: MutableState<String>
) {
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    var preview by remember { mutableStateOf<Preview?>(null) }
    val executor = ContextCompat.getMainExecutor(context)
    val cameraProvider = cameraProviderFuture.get()
    val textRecognizer =
        remember { TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS) }
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    Box {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f),
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                cameraProviderFuture.addListener({
                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .apply {
                            setAnalyzer(
                                cameraExecutor,
                                ObjectDetectorImageAnalyzer(textRecognizer, extractedText)
                            )
                        }
                    val cameraSelector = CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        imageAnalysis,
                        preview
                    )
                }, executor)
                preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                previewView
            }
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .align(Alignment.TopStart)
        ) {
            IconButton(
                onClick = { Toast.makeText(context, "Back Clicked", Toast.LENGTH_SHORT).show() }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "back",
                    tint = Color.White
                )
            }
        }
    }
}

class ObjectDetectorImageAnalyzer(
    private val textRecognizer: TextRecognizer,
    private val extractedText: MutableState<String>
) : ImageAnalysis.Analyzer {
    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

//            textRecognizer.process(image)
//                .addOnCompleteListener {
//                    if (it.isSuccessful) {
//                        extractedText.value = it.result?.text ?: ""
//                    }
//                    imageProxy.close()
//                }
            textRecognizer.process(image)
                .addOnCompleteListener {
                    if (it.isSuccessful) {

                        it.result?.let { res ->
                            val resultText = res.text
                            for (block in res.textBlocks) {
                                val blockText = block.text
                                val blockCornerPoints = block.cornerPoints
                                val blockFrame = block.boundingBox
                                Timber.d("blockText - $blockText")
                                Timber.d("blockCornerPoints - $blockCornerPoints")
                                Timber.d("blockFrame - $blockFrame")
                                for (line in block.lines) {
                                    val lineText = line.text
                                    val lineCornerPoints = line.cornerPoints
                                    val lineFrame = line.boundingBox
                                    Timber.d("lineText - $lineText")
                                    Timber.d("lineCornerPoints - $lineCornerPoints")
                                    Timber.d("lineFrame - $lineFrame")
                                    for (element in line.elements) {
                                        val elementText = element.text
                                        val elementCornerPoints = element.cornerPoints
                                        val elementFrame = element.boundingBox
                                        Timber.d("elementText - $elementText")
                                        Timber.d("elementCornerPoints - $elementCornerPoints")
                                        Timber.d("elementFrame - $elementFrame")
                                    }
                                }
                            }
                        }

                        extractedText.value = it.result?.text ?: ""
                    }
                    imageProxy.close()
                }
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    fun analyzeCapturedImage(capturedImage: Image?) {
        val image =
            capturedImage?.let { InputImage.fromMediaImage(it, 0) } // Assuming rotation is 0

        if (image != null) {
            textRecognizer.process(image)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        extractedText.value = it.result?.text ?: ""
                        Timber.d("extractedText: $extractedText")
                    }
                    capturedImage.close()
                }
        }
    }
}

class TextAnalyzer(
    private val textRecognizer: TextRecognizer
) : ImageAnalysis.Analyzer {
    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            // Pass image to an ML Kit Vision API
            val result = textRecognizer.process(image)
                .addOnSuccessListener { visionText ->
                    // Task completed successfully
                    Timber.d("visionText: ${visionText.text}\n")

                    visionText?.let { res ->
                        for (block in res.textBlocks) {
                            val blockText = block.text

                            // Check for specific keywords
                            if (blockText.contains("SUBTOTAL", ignoreCase = true)) {
                                // Extract subtotal from the current block
                                extractSubtotalFromBlock(block)

                                // Look for nearby lines for subtotal
                                val nearbyLines = findNearbyLines(res, block)
                                for (line in nearbyLines) {
                                    extractSubtotalFromLine(line)
                                }
                            } else if (blockText.contains(
                                    "TAX EXCLUSIVE TOTAL",
                                    ignoreCase = true
                                )
                            ) {
                                extractTaxExclusiveTotal(block)
                            }
                            Timber.d("blockText - $blockText")
                            for (line in block.lines) {
                                val lineText = line.text
                                Timber.d("lineText - $lineText")
                                for (element in line.elements) {
                                    val elementText = element.text
                                    Timber.d("elementText - $elementText")
                                }
                            }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    // Task failed with an exception
                    Timber.d("visionText error - ${e.message}")
                }

        }
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

    private fun extractSubtotalFromLine(line: Text.Line) {
        // Implement logic to extract subtotal information from the line
        val subtotalText = line.text
        Timber.d("Found Subtotal in Line: $subtotalText")
        // Add your logic to extract and handle the subtotal value
    }

    private fun extractTaxExclusiveTotal(block: Text.TextBlock) {
        // Implement logic to extract tax-exclusive total information from the block
        val taxExclusiveTotalText = block.text
        Timber.d("Found Tax Exclusive Total: $taxExclusiveTotalText")
        // Add your logic to extract and handle the tax-exclusive total value
    }
}