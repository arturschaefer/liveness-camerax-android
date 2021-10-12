package com.schaefer.livenesscamerax.camera.provider

import androidx.camera.core.ImageAnalysis

internal interface AnalyzerProvider {
    fun createAnalyzer(): ImageAnalysis
}
