package com.schaefer.camera.analyzer

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.schaefer.camera.processor.FrameProcessor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class CameraXAnalyzer(
    private val coroutineScope: CoroutineScope
) : ImageAnalysis.Analyzer {

    private val processors: MutableList<FrameProcessor> = mutableListOf()

    fun attachProcessor(vararg frameProcessor: FrameProcessor) {
        processors.addAll(frameProcessor)
    }

    override fun analyze(image: ImageProxy) {
        coroutineScope.launch {
            processors.forEach { processor ->
                processor.onFrameCaptured(image)
            }
        }
    }
}
