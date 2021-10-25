package com.schaefer.livenesscamerax.camera.provider.analyzer

import androidx.camera.core.ImageAnalysis

internal interface AnalyzerProvider {
    fun createAnalyzer(): ImageAnalysis
}
