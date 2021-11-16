package com.schaefer.camera.core.processor.face

import android.annotation.SuppressLint
import android.media.Image
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.face.Face
import com.schaefer.camera.core.detector.VisionFaceDetector
import com.schaefer.camera.domain.mapper.FaceToFaceResultMapper
import com.schaefer.camera.domain.model.FaceResult
import com.schaefer.core.extensions.getLuminosity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class FaceFrameProcessorImpl(
    private val coroutineScope: CoroutineScope,
    private val mapper: FaceToFaceResultMapper,
    private val detector: VisionFaceDetector,
) : FaceFrameProcessor {

    private val facesBroadcastChannel = MutableSharedFlow<List<FaceResult>>()

    override fun observeFaceList(): Flow<List<FaceResult>> = facesBroadcastChannel.asSharedFlow()

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override suspend fun onFrameCaptured(imageProxy: ImageProxy) {
        detector.detect(imageProxy) {
            coroutineScope.launch {
                facesBroadcastChannel.emit(
                    prepareResultWithLuminosity(it, imageProxy)
                )
            }
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
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
