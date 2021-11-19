package com.schaefer.camera.core.detector

import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.face.Face

internal interface FrameFaceDetector {
    fun detect(imageProxy: ImageProxy, processImage: (List<Face>) -> Unit)
}
