package com.schaefer.camera.core.analyzer

import androidx.camera.core.ImageAnalysis
import androidx.lifecycle.lifecycleScope
import com.schaefer.camera.core.processor.face.FaceFrameProcessor
import com.schaefer.camera.core.processor.luminosity.LuminosityFrameProcessor
import com.schaefer.camera.di.CameraModule.container
import com.schaefer.camera.di.CameraModule.lifecycleOwner
import com.schaefer.domain.model.AnalyzeTypeDomain

internal class AnalyzeProvider {

    class Builder() {
        var analyzeType = AnalyzeTypeDomain.FACE_PROCESSOR
        var faceFrameProcessor: FaceFrameProcessor = container.provideFaceFrameProcessor()
        var cameraExecutors = container.provideExecutorService()
        var luminosityFrameProcessor: LuminosityFrameProcessor =
            container.provideLuminosityFrameProcessor()

        private fun getAnalyzerType() =
            when (analyzeType) {
                AnalyzeTypeDomain.FACE_PROCESSOR -> CameraXAnalyzer(
                    lifecycleOwner.lifecycleScope
                ).apply {
                    attachProcessor(
                        faceFrameProcessor
                    )
                }
                AnalyzeTypeDomain.LUMINOSITY -> CameraXAnalyzer(
                    lifecycleOwner.lifecycleScope
                ).apply {
                    attachProcessor(
                        luminosityFrameProcessor
                    )
                }
                AnalyzeTypeDomain.COMPLETE -> CameraXAnalyzer(
                    lifecycleOwner.lifecycleScope
                ).apply {
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
