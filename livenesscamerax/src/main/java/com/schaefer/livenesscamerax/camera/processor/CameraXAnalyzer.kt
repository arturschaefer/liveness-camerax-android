package com.schaefer.livenesscamerax.camera.processor

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

internal class CameraXAnalyzer : ImageAnalysis.Analyzer {

    private val processors: MutableList<FrameProcessor> = mutableListOf()

    fun attachProcessor(frameProcessor: FrameProcessor) {
        processors.add(frameProcessor)
    }

    override fun analyze(image: ImageProxy) {
        CoroutineScope(IO).launch {
            processors.forEach { processor ->
                processor.onFrameCaptured(image)
            }
        }
    }
}