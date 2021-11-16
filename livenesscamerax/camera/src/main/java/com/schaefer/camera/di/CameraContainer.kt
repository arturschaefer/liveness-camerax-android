package com.schaefer.camera.di

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.schaefer.camera.core.detector.VisionFaceDetector
import com.schaefer.camera.core.processor.face.FaceFrameProcessor
import com.schaefer.camera.core.processor.face.FaceFrameProcessorImpl
import com.schaefer.camera.core.processor.luminosity.LuminosityFrameProcessor
import com.schaefer.camera.core.processor.luminosity.LuminosityFrameProcessorFactory
import com.schaefer.camera.domain.mapper.FaceToFaceResultMapper
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraContainer {
    fun provideExecutorService(): ExecutorService {
        return Executors.newSingleThreadExecutor()
    }

    fun provideLuminosityFrameProcessor(): LuminosityFrameProcessor {
        return LuminosityFrameProcessorFactory.create()
    }

    fun provideFaceFrameProcessor(
        lifecycleOwner: LifecycleOwner
    ): FaceFrameProcessor {
        return FaceFrameProcessorImpl(
            lifecycleOwner.lifecycleScope,
            FaceToFaceResultMapper(),
            VisionFaceDetector()
        )
    }
}
