package com.schaefer.livenesscamerax.camera.detector

import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.face.Face
import kotlinx.coroutines.channels.Channel

internal interface FrameFaceDetector {
    fun detect(imageProxy: ImageProxy): Channel<List<Face>>
}