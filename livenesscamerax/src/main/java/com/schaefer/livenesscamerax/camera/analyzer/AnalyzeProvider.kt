package com.schaefer.livenesscamerax.camera.analyzer

import androidx.camera.core.ImageAnalysis
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.schaefer.camera.analyzer.CameraXAnalyzer
import com.schaefer.camera.processor.face.FaceFrameProcessor
import com.schaefer.camera.processor.luminosity.LuminosityFrameProcessor
import com.schaefer.livenesscamerax.di.LibraryModule.container
import com.schaefer.livenesscamerax.domain.model.AnalyzeType

internal class AnalyzeProvider {

    class Builder(private val lifecycleOwner: LifecycleOwner) {
        var analyzeType = AnalyzeType.FACE_PROCESSOR
        var faceFrameProcessor: FaceFrameProcessor =
            container.provideFaceFrameProcessor(lifecycleOwner)
        var luminosityFrameProcessor: LuminosityFrameProcessor =
            container.provideLuminosityFrameProcessor()
        var cameraExecutors = container.provideExecutorService()

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
