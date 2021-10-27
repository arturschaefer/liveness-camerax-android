package com.schaefer.livenesscamerax.camera

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
import com.schaefer.livenesscamerax.camera.callback.CameraXCallback
import com.schaefer.livenesscamerax.camera.provider.analyzer.AnalyzeProvider
import com.schaefer.livenesscamerax.camera.provider.file.FileHandler
import com.schaefer.livenesscamerax.core.exceptions.LivenessCameraXException
import com.schaefer.livenesscamerax.core.extensions.getCameraSelector
import com.schaefer.livenesscamerax.core.extensions.orFalse
import com.schaefer.livenesscamerax.di.LibraryModule.application
import com.schaefer.livenesscamerax.presentation.model.CameraSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import timber.log.Timber
import java.util.concurrent.Future

@FlowPreview
@ExperimentalCoroutinesApi
internal class CameraXImpl(
    private val settings: CameraSettings,
    private val cameraXCallback: CameraXCallback,
    private val lifecycleOwner: LifecycleOwner,
    private val fileHandler: FileHandler,
) : CameraX, DefaultLifecycleObserver {

    private val imageCapture by lazy {
        ImageCapture.Builder().build().apply {
            targetRotation = Surface.ROTATION_0
        }
    }

//    private val cameraExecutorService: ExecutorService by lazy { container.provideCameraExecutor() }
    private val analyzerProvider by lazy {
        AnalyzeProvider.Builder(lifecycleOwner).apply {
            analyzeType = settings.analyzeType
        }
    }
    private var camera: Camera? = null

    //region - Camera settings and creators
    override fun getFacesFlowable() = analyzerProvider.faceFrameProcessor.getData()

    override fun getLuminosity() = analyzerProvider.luminosityFrameProcessor.getLuminosity()

    override fun getLifecycleObserver() = this

    override fun deleteAllPictures() = fileHandler.deleteStorageFiles()

    override fun getAllPictures(): List<String> = fileHandler.getPathOfAllPhotos()

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
        val photoFile = fileHandler.getPhotoFile()

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
        val cameraSelector = settings.getCameraSelector()

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
