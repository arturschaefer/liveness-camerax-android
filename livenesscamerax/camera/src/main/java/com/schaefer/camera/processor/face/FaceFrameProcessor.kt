package com.schaefer.camera.processor.face

import com.schaefer.camera.model.FaceResult
import com.schaefer.camera.processor.FrameProcessor
import kotlinx.coroutines.flow.Flow

interface FaceFrameProcessor : FrameProcessor {
    fun observeFaceList(): Flow<List<FaceResult>>
}
