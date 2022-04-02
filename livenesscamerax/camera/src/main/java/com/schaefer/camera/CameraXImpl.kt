package com.schaefer.camera

import android.view.Surface
import androidx.camera.core.Camera
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.schaefer.camera.core.analyzer.AnalyzeProvider
import com.schaefer.camera.core.callback.CameraXCallback
import com.schaefer.camera.di.CameraModule.application
import com.schaefer.camera.domain.mapper.toCameraSelector
import com.schaefer.core.extensions.orFalse
import com.schaefer.domain.model.CameraSettingsDomain
import com.schaefer.domain.model.exceptions.LivenessCameraXException
import com.schaefer.domain.repository.FileRepository
import java.util.concurrent.Future

internal class CameraXImpl(
    private val settings: CameraSettingsDomain,
    private val cameraXCallback: CameraXCallback,
    private val lifecycleOwner: LifecycleOwner,
    private val fileRepository: FileRepository,
) : CameraX, DefaultLifecycleObserver {

    private val imageCapture by lazy {
        ImageCapture.Builder().build().apply {
            targetRotation = Surface.ROTATION_0
        }
    }

    private val analyzerProvider by lazy {
        AnalyzeProvider.Builder().apply { analyzeType = settings.analyzeType }
    }
    private var camera: Camera? = null

    //region - Camera settings and creators
    override fun observeFaceList() = analyzerProvider.faceFrameProcessor.observeFaceList()

    override fun observeLuminosity() = analyzerProvider.luminosityFrameProcessor.observeLuminosity()

    override fun getLifecycleObserver() = this

    override fun deleteAllPictures() = fileRepository.deleteStorageFiles()

    override fun getAllPictures(): List<String> = fileRepository.getPathOfAllPhotos()

    override fun startCamera(cameraPreviewView: PreviewView) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(application)
        val runnableSetup = Runnable {
            setupCamera(cameraProviderFuture, cameraPreviewView, lifecycleOwner)
        }
        cameraProviderFuture.addListener(
            runnableSetup,
            ContextCompat.getMainExecutor(application)
        )
    }

    override fun takePicture(takenByUser: Boolean) {
        val photoFile = fileRepository.getPhotoFile()
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(application),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    cameraXCallback.onSuccess(photoFile, takenByUser)
                }

                override fun onError(exception: ImageCaptureException) {
                    cameraXCallback.onError(exception)
                }
            }
        )
    }

    override fun enableFlash(enabled: Boolean) {
        camera?.let {
            if (it.cameraInfo.hasFlashUnit().orFalse()) it.cameraControl.enableTorch(enabled)
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        analyzerProvider.cameraExecutors.shutdown()
        if (!BuildConfig.DEBUG) fileRepository.deleteStorageFiles()
        super.onDestroy(owner)
    }

    @Suppress("TooGenericExceptionCaught")
    private fun setupCamera(
        cameraProviderFuture: Future<ProcessCameraProvider>,
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner
    ) {
        camera = null
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder().build().also { preview ->
            preview.setSurfaceProvider(previewView.surfaceProvider)
        }
        val analyzer = analyzerProvider.build()
        val cameraSelector = settings.cameraLens.toCameraSelector()

        try {
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture,
                analyzer
            )

            enableFlash(settings.isFlashEnabled)
        } catch (ex: Exception) {
            cameraXCallback.onError(
                LivenessCameraXException.StartCameraException(
                    ex.message,
                    ex.cause
                )
            )
        }
    }
    //endregion
}
