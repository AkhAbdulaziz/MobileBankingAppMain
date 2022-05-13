package uz.targetsoftwaredevelopment.mobilebankingapp.utils

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class TextReaderAnalyzer(val listener: (String) -> Unit) : ImageAnalysis.Analyzer {
    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        try {
            imageProxy.image?.let {
                readTextFromImage(
                    InputImage.fromMediaImage(
                        it,
                        imageProxy.imageInfo.rotationDegrees
                    ), imageProxy
                )
            }
        } catch (e: Exception) {
            Log.d("TTT", "TextReaderAnalyzer = ${e.message}")
        }
    }


    private fun readTextFromImage(image: InputImage, imageProxy: ImageProxy) {
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            .process(image)
            .addOnSuccessListener {
                Log.d("KKK", it.text)
                processTextFromImage(it,imageProxy)
                imageProxy.close()
            }
            .addOnFailureListener {
                Log.d("TTT", "readTextFromImage.exception = ${it.message}")
            }
    }

    private fun processTextFromImage(visionText: Text, imageProxy: ImageProxy) {
        for (block in visionText.textBlocks) {
            for (line in block.lines) {
                for (element in line.elements) {
                    Log.d("PPP", element.text)
                    listener(element.text)
                }
            }
        }
    }
}