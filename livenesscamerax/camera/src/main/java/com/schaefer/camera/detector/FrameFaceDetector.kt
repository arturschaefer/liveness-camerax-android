package com.schaefer.camera.detector

import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.face.Face

interface FrameFaceDetector {
    fun detect(imageProxy: ImageProxy, processImage: (List<Face>) -> Unit)
}
