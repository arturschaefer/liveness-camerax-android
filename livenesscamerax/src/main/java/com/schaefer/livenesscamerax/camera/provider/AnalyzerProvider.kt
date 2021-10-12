package com.schaefer.livenesscamerax.camera.provider

import androidx.camera.core.ImageAnalysis

interface AnalyzerProvider {
    fun createAnalyzer(): ImageAnalysis
}