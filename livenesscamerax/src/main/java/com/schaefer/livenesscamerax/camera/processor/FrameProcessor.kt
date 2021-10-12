package com.schaefer.livenesscamerax.camera.processor

import androidx.camera.core.ImageProxy

internal interface FrameProcessor {
    suspend fun onFrameCaptured(imageProxy: ImageProxy)
}
