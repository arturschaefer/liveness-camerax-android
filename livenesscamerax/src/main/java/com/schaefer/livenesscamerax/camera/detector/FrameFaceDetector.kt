package com.schaefer.livenesscamerax.camera.detector

import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.face.Face
import kotlinx.coroutines.channels.BroadcastChannel

internal interface FrameFaceDetector {
    suspend fun detect(imageProxy: ImageProxy): BroadcastChannel<List<Face>>
}