package com.schaefer.livenesscamerax.camera.provider

import androidx.camera.core.ImageAnalysis
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.schaefer.livenesscamerax.camera.processor.CameraXAnalyzer
import com.schaefer.livenesscamerax.camera.processor.FrameProcessor
import com.schaefer.livenesscamerax.domain.model.AnalyzeType
import java.util.concurrent.Executor

internal class AnalyzerProviderImpl(
    private val analyzeType: AnalyzeType,
    private val lifecycleOwner: LifecycleOwner,
    private val cameraExecutors: Executor,
    private val frameFaceProcessor: FrameProcessor,
    private val luminosityFrameProcessor: FrameProcessor,
) : AnalyzerProvider {

    override fun createAnalyzer(): ImageAnalysis {
        return ImageAnalysis.Builder()
            .build()
            .also { imageAnalysis ->
                imageAnalysis.setAnalyzer(
                    cameraExecutors,
                    getAnalyzerType()
                )
            }
    }

    private fun getAnalyzerType() =
        when (analyzeType) {
            AnalyzeType.FACE_PROCESSOR -> CameraXAnalyzer(lifecycleOwner.lifecycleScope).apply {
                attachProcessor(
                    frameFaceProcessor
                )
            }
            AnalyzeType.LUMINOSITY -> CameraXAnalyzer(lifecycleOwner.lifecycleScope).apply {
                attachProcessor(
                    luminosityFrameProcessor
                )
            }
            AnalyzeType.COMPLETE -> CameraXAnalyzer(lifecycleOwner.lifecycleScope).apply {
                attachProcessor(
                    luminosityFrameProcessor,
                    frameFaceProcessor
                )
            }
        }
}
