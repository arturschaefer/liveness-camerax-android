package com.schaefer.livenesscamerax.camera.processor.face

import com.schaefer.livenesscamerax.camera.processor.FrameProcessor
import com.schaefer.livenesscamerax.domain.model.FaceResult
import kotlinx.coroutines.flow.Flow

internal interface FaceFrameProcessor : FrameProcessor {
    fun observeFaceList(): Flow<List<FaceResult>>
}
