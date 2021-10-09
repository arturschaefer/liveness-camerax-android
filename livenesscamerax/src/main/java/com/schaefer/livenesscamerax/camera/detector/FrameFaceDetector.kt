package com.schaefer.livenesscamerax.camera.detector

import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.face.Face

internal interface FrameFaceDetector {
    fun detect(imageProxy: ImageProxy, proccessImage: (List<Face>) -> Unit)
}
