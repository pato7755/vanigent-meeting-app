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

}