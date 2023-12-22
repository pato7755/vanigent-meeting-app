package com.vanigent.meetingapp.ui.coordinatorlogin

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognizer
import timber.log.Timber

interface TextRecognitionCallback {
    fun onSuccess(visionText: Text)
    fun onFailure(exception: Exception)
}

class TextAnalyzer(
    private val textRecognizer: TextRecognizer,
//    private val extractedText: MutableMap<String, String>,
    private val callback: TextRecognitionCallback
) : ImageAnalysis.Analyzer {
    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            // Pass image to an ML Kit Vision API
            textRecognizer.process(image)
                .addOnSuccessListener { visionText ->
                    // Task completed successfully
                    callback.onSuccess(visionText)

                    Timber.d("visionText: ${visionText.text}\n")



                }
                .addOnFailureListener { e ->
                    // Task failed with an exception
                    callback.onFailure(e)
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

}