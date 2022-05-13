package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.screens.card

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.databinding.ScreenScanCardBinding
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.TextReaderAnalyzer
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.checkPermissions
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.scope
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.showFancyToast
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class ScanCardScreen : Fragment(R.layout.screen_scan_card) {
    private val binding by viewBinding(ScreenScanCardBinding::bind)

    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().checkPermissions(arrayOf(Manifest.permission.CAMERA)) {
            startCamera()
            outputDirectory = getOutputDirectory()
            binding.btnTakePhoto.setOnClickListener {
                takePhoto()
            }
        }
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun takePhoto() {
        Toast.makeText(requireContext(), "takePhoto", Toast.LENGTH_SHORT).show()
        val imageCapture = imageCapture ?: return
        val photoFile = File(outputDirectory, "my picture ${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    showFancyToast(
                        "Saved",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.INFO
                    )
                }

                override fun onError(exception: ImageCaptureException) {
                    showFancyToast(
                        "Failed",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR
                    )
                }
            })
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            }
            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            val imageAnalyzer = ImageAnalysis.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, TextReaderAnalyzer {
                        Log.d("TTT", "Average luminosity: $it")

                    })
                }
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture,
                    imageAnalyzer
                )
            } catch (e: Exception) {
                Log.d("TTT", "Exception = ${e.message}")
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun getOutputDirectory(): File {
        val mediaDir = requireActivity().externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireActivity().filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}