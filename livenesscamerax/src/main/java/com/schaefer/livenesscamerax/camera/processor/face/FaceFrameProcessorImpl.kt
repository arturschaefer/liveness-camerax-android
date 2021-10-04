package com.schaefer.livenesscamerax.camera.processor.face

import android.annotation.SuppressLint
import android.media.Image
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.face.Face
import com.schaefer.livenesscamerax.camera.detector.VisionFaceDetector
import com.schaefer.livenesscamerax.core.extensions.getLuminosity
import com.schaefer.livenesscamerax.core.mapper.FaceToFaceResultMapper
import com.schaefer.livenesscamerax.domain.model.FaceResult
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import timber.log.Timber

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@FlowPreview
internal class FaceFrameProcessorImpl : FaceFrameProcessor {

    private val mapper: FaceToFaceResultMapper by lazy {
        FaceToFaceResultMapper()
    }

    private val detector: VisionFaceDetector by lazy {
        VisionFaceDetector()
    }

    private val publishSubject = BroadcastChannel<List<FaceResult>>(1)

    override fun getData(): Flow<List<FaceResult>> = publishSubject.asFlow()

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onFrameCaptured(imageProxy: ImageProxy) {
        Timber.d("Imageproxy ${imageProxy.toString()}")
        CoroutineScope(IO).launch {
            detector.detect(imageProxy).consumeAsFlow().collect { frame ->
                publishSubject.send(
                    prepareToPublish(frame, imageProxy)
                )
            }
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun prepareToPublish(
        listFace: List<Face>,
        imageProxy: ImageProxy
    ): List<FaceResult> = listFace.map { face ->
        addLuminosity(mapper.map(face), imageProxy.image)
    }

    private fun addLuminosity(faceResult: FaceResult, image: Image?): FaceResult {
        return image?.let { faceResult.copy(luminosity = image.getLuminosity()) } ?: faceResult
    }
}