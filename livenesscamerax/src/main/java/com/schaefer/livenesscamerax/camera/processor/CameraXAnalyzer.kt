package com.schaefer.livenesscamerax.camera.processor

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy

internal class CameraXAnalyzer : ImageAnalysis.Analyzer {

    private val processors: MutableList<FrameProcessor> = mutableListOf()

    fun attachProcessor(frameProcessor: FrameProcessor) {
        processors.add(frameProcessor)
    }

    override fun analyze(image: ImageProxy) {
        processors.forEach { processor ->
            processor.onFrameCaptured(image)
        }
    }
}