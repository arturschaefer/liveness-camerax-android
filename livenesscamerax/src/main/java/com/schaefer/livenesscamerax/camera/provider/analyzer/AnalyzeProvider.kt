package com.schaefer.livenesscamerax.camera.provider.analyzer

import androidx.camera.core.ImageAnalysis
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.schaefer.livenesscamerax.camera.processor.CameraXAnalyzer
import com.schaefer.livenesscamerax.camera.processor.FrameProcessor
import com.schaefer.livenesscamerax.di.LibraryModule.container
import com.schaefer.livenesscamerax.domain.model.AnalyzeType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.concurrent.Executor

@ExperimentalCoroutinesApi
internal class AnalyzeProvider {

    class Builder(private val lifecycleOwner: LifecycleOwner) {
        var analyzeType = AnalyzeType.FACE_PROCESSOR
        var cameraExecutors: Executor = container.provideSingleThreadExecutor
        var faceFrameProcessor: FrameProcessor = container.provideFaceFrameProcessor(lifecycleOwner)
        var luminosityFrameProcessor: FrameProcessor = container.provideLuminosityFrameProcessor

        private fun getAnalyzerType() =
            when (analyzeType) {
                AnalyzeType.FACE_PROCESSOR -> CameraXAnalyzer(lifecycleOwner.lifecycleScope).apply {
                    attachProcessor(
                        faceFrameProcessor
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
                        faceFrameProcessor
                    )
                }
            }

        fun build(): ImageAnalysis {
            return ImageAnalysis.Builder()
                .build()
                .also { imageAnalysis ->
                    imageAnalysis.setAnalyzer(
                        cameraExecutors,
                        getAnalyzerType()
                    )
                }
        }
    }
}
