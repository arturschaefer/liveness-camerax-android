package com.schaefer.camera.core.processor

import androidx.camera.core.ImageProxy

interface FrameProcessor {
    suspend fun onFrameCaptured(imageProxy: ImageProxy)
}
