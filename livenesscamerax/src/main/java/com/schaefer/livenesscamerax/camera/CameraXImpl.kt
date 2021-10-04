package com.schaefer.livenesscamerax.camera

import android.content.Context
import android.net.Uri
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.schaefer.livenesscamerax.camera.processor.CameraXAnalyzer
import com.schaefer.livenesscamerax.camera.processor.face.FaceFrameProcessor
import com.schaefer.livenesscamerax.camera.processor.face.FaceFrameProcessorImpl
import com.schaefer.livenesscamerax.camera.processor.luminosity.LuminosityFrameProcessor
import com.schaefer.livenesscamerax.camera.processor.luminosity.LuminosityFrameProcessorImpl
import com.schaefer.livenesscamerax.domain.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
private const val DIR_NAME = "photos_liveness"
private const val SUFFIX_PHOTO_FILE = ".jpg"

@FlowPreview
@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
internal class CameraXImpl(
    private val settings: CameraSettings,
    private val analyzeType: AnalyzeType = AnalyzeType.FACE_PROCESSOR,
    private val cameraXCallback: CameraXCallback,
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
) : CameraX, DefaultLifecycleObserver {

    //region - Properties
    private var camera: Camera? = null
    private val imageCapture: ImageCapture by lazy {
        createImageCapture()
    }
    private val cameraExecutor: ExecutorService by lazy {
        createCameraExecutor()
    }
    private val frameFaceProcessor: FaceFrameProcessor by lazy {
        FaceFrameProcessorImpl()
    }

    private val luminosityFrameProcessor: LuminosityFrameProcessor by lazy {
        LuminosityFrameProcessorImpl()
    }

    //TODO put file manager outside of this class
    private val outputDirectory: File? by lazy {
        createStorageOutput()
    }

    private fun createStorageOutput(): File? = when (settings.storageType) {
        StorageType.INTERNAL -> context.getDir(DIR_NAME, Context.MODE_PRIVATE)
        StorageType.EXTERNAL -> context.getExternalFilesDir(DIR_NAME)
    }

    private val simpleDateFormat = SimpleDateFormat(
        FILENAME_FORMAT, Locale.US
    )
    //endregion

    //region - Camera settings and creators
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

    private fun setupCamera(
        cameraProviderFuture: Future<ProcessCameraProvider>,
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner
    ) {
        camera = null
        val cameraProvider = cameraProviderFuture.get()
        val preview = createPreview(previewView.surfaceProvider)
        val analyzer = createAnalyzer()
        val cameraSelector = createCameraSelector()

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
        }
    }

    private fun createCameraExecutor(): ExecutorService {
        return Executors.newSingleThreadExecutor()
    }

    private fun createCameraSelector(): CameraSelector {
        return when (settings.cameraLens) {
            CameraLens.DEFAULT_BACK_CAMERA -> CameraSelector.DEFAULT_BACK_CAMERA
            CameraLens.DEFAULT_FRONT_CAMERA -> CameraSelector.DEFAULT_FRONT_CAMERA
        }
    }

    private fun createImageCapture(): ImageCapture {
        return ImageCapture.Builder().build()
    }

    private fun createAnalyzer(): ImageAnalysis {
        return ImageAnalysis.Builder()
            .build()
            .also { imageAnalysis ->
                imageAnalysis.setAnalyzer(
                    cameraExecutor,
                    getAnalyzerType()
                )
            }

    }

    private fun getAnalyzerType() = when (analyzeType) {
        AnalyzeType.FACE_PROCESSOR -> CameraXAnalyzer().apply {
            attachProcessor(
                frameFaceProcessor
            )
        }
        AnalyzeType.LUMINOSITY -> CameraXAnalyzer().apply {
            attachProcessor(
                luminosityFrameProcessor
            )
        }
        AnalyzeType.COMPLETE -> CameraXAnalyzer().apply {
            attachProcessor(
                luminosityFrameProcessor
            )
            attachProcessor(
                frameFaceProcessor
            )
        }
    }


    private fun createPreview(surfaceProvider: Preview.SurfaceProvider): Preview {
        return Preview.Builder().build().also { preview ->
            preview.setSurfaceProvider(surfaceProvider)
        }
    }

    override fun takePicture() {
        // Create time-stamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            simpleDateFormat.format(System.currentTimeMillis()) + SUFFIX_PHOTO_FILE
        )

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

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time-stamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
               FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Timber.e("Photo capture failed: ${exc.message}")
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    Timber.d("Photo capture succeeded: $savedUri")
                }
            }
        )
    }

    override fun enableFlash(enabled: Boolean) {
        //TODO
    }
    //endregion

    //region - Observers and listeners
    fun getFacesFlowable(): Flow<List<FaceResult>> {
        return frameFaceProcessor.getData()
    }

    fun getLuminosity(): Flow<Double>{
        return luminosityFrameProcessor.getLuminosity()
    }


    override fun onDestroy(owner: LifecycleOwner) {
        cameraExecutor.shutdown()
        super.onDestroy(owner)
    }
    //endregion
}