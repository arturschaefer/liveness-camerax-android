package com.schaefer.livenesscamerax.camera

import android.content.Context
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
import androidx.lifecycle.lifecycleScope
import com.schaefer.livenesscamerax.camera.callback.CameraXCallback
import com.schaefer.livenesscamerax.camera.processor.face.FaceFrameProcessor
import com.schaefer.livenesscamerax.camera.processor.face.FaceFrameProcessorImpl
import com.schaefer.livenesscamerax.camera.processor.luminosity.LuminosityFrameProcessor
import com.schaefer.livenesscamerax.camera.processor.luminosity.LuminosityFrameProcessorImpl
import com.schaefer.livenesscamerax.camera.provider.AnalyzerProviderImpl
import com.schaefer.livenesscamerax.camera.provider.FileProvider
import com.schaefer.livenesscamerax.camera.provider.FileProviderImpl
import com.schaefer.livenesscamerax.core.exceptions.LivenessCameraXException
import com.schaefer.livenesscamerax.core.extensions.getCameraSelector
import com.schaefer.livenesscamerax.domain.model.CameraSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import java.util.concurrent.Executors
import java.util.concurrent.Future

@FlowPreview
@ExperimentalCoroutinesApi
internal class CameraXImpl(
    private val settings: CameraSettings,
    private val cameraXCallback: CameraXCallback,
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
) : CameraX, DefaultLifecycleObserver {

    private val cameraExecutors by lazy { Executors.newSingleThreadExecutor() }
    private val imageCapture by lazy { ImageCapture.Builder().build().apply {
        targetRotation = Surface.ROTATION_0
    } }
    private val frameFaceProcessor: FaceFrameProcessor by lazy {
        FaceFrameProcessorImpl(lifecycleOwner.lifecycleScope)
    }
    private val luminosityFrameProcessor: LuminosityFrameProcessor by lazy {
        LuminosityFrameProcessorImpl()
    }

    //TODO create a fileProvider factory
    private val fileProvider: FileProvider by lazy {
        FileProviderImpl(settings, context)
    }

    private val analyzerProvider by lazy {
        AnalyzerProviderImpl(
            settings.analyzeType,
            lifecycleOwner,
            cameraExecutors,
            frameFaceProcessor,
            luminosityFrameProcessor
        )
    }
    private var camera: Camera? = null

    //region - Camera settings and creators
    override fun getFacesFlowable() = frameFaceProcessor.getData()

    override fun getLuminosity(): Flow<Double> = luminosityFrameProcessor.getLuminosity()

    override fun getLifecycleObserver() = this

    override fun deleteAllPictures() = fileProvider.deleteStorageFiles()

    override fun getAllPictures(): List<String> = fileProvider.getPathOfAllPhotos()

    override fun startCamera(cameraPreviewView: PreviewView) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        val runnableSetup = Runnable {
            setupCamera(cameraProviderFuture, cameraPreviewView, lifecycleOwner)
        }
        cameraProviderFuture.addListener(
            runnableSetup,
            ContextCompat.getMainExecutor(context)
        )
    }

    override fun takePicture() {
        // Create time-stamped output file to hold the image
        val photoFile = fileProvider.getPhotoFile()

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    cameraXCallback.onSuccess(photoFile)
                }

                override fun onError(exception: ImageCaptureException) {
                    cameraXCallback.onError(exception)
                }
            }
        )
    }

    override fun enableFlash(enabled: Boolean) {
        // TODO
    }

    override fun onDestroy(owner: LifecycleOwner) {
        cameraExecutors.shutdown()
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
        val preview = createPreview(previewView.surfaceProvider)
        val analyzer = analyzerProvider.createAnalyzer()
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

    private fun createPreview(surfaceProvider: Preview.SurfaceProvider): Preview {
        return Preview.Builder().build().also { preview ->
            preview.setSurfaceProvider(surfaceProvider)
        }
    }
    //endregion
}
