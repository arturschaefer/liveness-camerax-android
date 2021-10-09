package com.schaefer.livenesscamerax.camera.processor

import androidx.camera.core.ImageProxy

interface FrameProcessor {
    suspend fun onFrameCaptured(imageProxy: ImageProxy)
}
