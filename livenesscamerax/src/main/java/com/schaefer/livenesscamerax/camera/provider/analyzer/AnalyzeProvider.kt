package com.schaefer.livenesscamerax.camera.provider.analyzer

import androidx.camera.core.ImageAnalysis
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.schaefer.livenesscamerax.camera.processor.CameraXAnalyzer
import com.schaefer.livenesscamerax.camera.processor.face.FaceFrameProcessor
import com.schaefer.livenesscamerax.camera.processor.luminosity.LuminosityFrameProcessor
import com.schaefer.livenesscamerax.di.LibraryModule.container
import com.schaefer.livenesscamerax.domain.model.AnalyzeType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.concurrent.ExecutorService

@ExperimentalCoroutinesApi
internal class AnalyzeProvider(builder: Builder) {

    private val faceFrameProcessor: FaceFrameProcessor = builder.faceFrameProcessor
    private val luminosityFrameProcessor: LuminosityFrameProcessor =
        builder.luminosityFrameProcessor
    private val cameraExecutors: ExecutorService = builder.cameraExecutors

    class Builder(private val lifecycleOwner: LifecycleOwner) {
        var analyzeType = AnalyzeType.FACE_PROCESSOR
        var faceFrameProcessor: FaceFrameProcessor =
            container.provideFaceFrameProcessor(lifecycleOwner)
        var luminosityFrameProcessor: LuminosityFrameProcessor =
            container.provideLuminosityFrameProcessor()
        var cameraExecutors = container.provideCameraExecutor()

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
