package com.schaefer.camera.core.processor.face

import com.schaefer.camera.core.processor.FrameProcessor
import com.schaefer.camera.domain.model.FaceResult
import kotlinx.coroutines.flow.Flow

internal interface FaceFrameProcessor : FrameProcessor {
    fun observeFaceList(): Flow<List<FaceResult>>
}
