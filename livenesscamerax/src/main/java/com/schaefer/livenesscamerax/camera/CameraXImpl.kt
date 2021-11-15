package com.schaefer.livenesscamerax.camera

import android.view.Surface
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.schaefer.camera.CameraX
import com.schaefer.camera.callback.CameraXCallback
import com.schaefer.core.extensions.orFalse
import com.schaefer.core.mapper.Mapper
import com.schaefer.domain.model.exceptions.LivenessCameraXException
import com.schaefer.domain.repository.FileRepository
import com.schaefer.livenesscamerax.BuildConfig
import com.schaefer.livenesscamerax.camera.analyzer.AnalyzeProvider
import com.schaefer.livenesscamerax.di.LibraryModule.application
import com.schaefer.livenesscamerax.domain.model.CameraLens
import com.schaefer.livenesscamerax.presentation.model.CameraSettings
import timber.log.Timber
import java.util.concurrent.Future

internal class CameraXImpl(
    private val settings: CameraSettings,
    private val cameraXCallback: CameraXCallback,
    private val lifecycleOwner: LifecycleOwner,
    private val cameraLensToCameraSelectorMapper: Mapper<CameraLens, CameraSelector>,
    private val fileRepository: FileRepository,
) : CameraX, DefaultLifecycleObserver {

    private val imageCapture by lazy {
        ImageCapture.Builder().build().apply {
            targetRotation = Surface.ROTATION_0
        }
    }

    private val analyzerProvider by lazy {
        AnalyzeProvider.Builder(lifecycleOwner).apply {
            analyzeType = settings.analyzeType
        }
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
        // Create time-stamped output file to hold the image
        val photoFile = fileRepository.getPhotoFile()

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
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
        val cameraSelector = cameraLensToCameraSelectorMapper.map(settings.cameraLens)

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
            Timber.e(ex.toString())
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
