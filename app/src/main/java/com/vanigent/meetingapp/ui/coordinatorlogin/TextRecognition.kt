package com.vanigent.meetingapp.ui.coordinatorlogin

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognizer
import timber.log.Timber

class TextAnalyzer(
    private val textRecognizer: TextRecognizer
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