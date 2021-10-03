package com.schaefer.livenesscamerax.camera.processor

import androidx.camera.core.ImageProxy

interface FrameProcessor {
    fun onFrameCaptured(imageProxy: ImageProxy)
}