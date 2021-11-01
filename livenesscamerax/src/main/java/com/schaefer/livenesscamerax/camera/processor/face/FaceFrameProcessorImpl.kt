package com.schaefer.livenesscamerax.camera.processor.face

import android.annotation.SuppressLint
import android.media.Image
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.face.Face
import com.schaefer.livenesscamerax.camera.detector.VisionFaceDetector
import com.schaefer.livenesscamerax.core.extensions.getLuminosity
import com.schaefer.livenesscamerax.domain.mapper.FaceToFaceResultMapper
import com.schaefer.livenesscamerax.domain.model.FaceResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
internal class FaceFrameProcessorImpl(
    private val coroutineScope: CoroutineScope,
    private val mapper: FaceToFaceResultMapper,
    private val detector: VisionFaceDetector,
) : FaceFrameProcessor {

    private val facesBroadcastChannel = BroadcastChannel<List<FaceResult>>(Channel.BUFFERED)

    override fun getData(): Flow<List<FaceResult>> =
        facesBroadcastChannel.openSubscription().consumeAsFlow()

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override suspend fun onFrameCaptured(imageProxy: ImageProxy) {
        detector.detect(imageProxy) {
            coroutineScope.launch {
                facesBroadcastChannel.send(
                    prepareResultWithLuminosity(it, imageProxy)
                )
            }
        }
    }

    @SuppressLint("UnsafeExperimentalUsageError", "UnsafeOptInUsageError")
    private fun prepareResultWithLuminosity(
        listFace: List<Face>,
        imageProxy: ImageProxy
    ): List<FaceResult> = listFace.map { face ->
        addLuminosity(mapper.map(face), imageProxy.image)
    }

    private fun addLuminosity(faceResult: FaceResult, image: Image?): FaceResult {
        return image?.let { faceResult.copy(luminosity = image.getLuminosity()) } ?: faceResult
    }
}
