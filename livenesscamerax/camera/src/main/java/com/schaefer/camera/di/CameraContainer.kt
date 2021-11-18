package com.schaefer.camera.di

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.schaefer.camera.core.detector.VisionFaceDetector
import com.schaefer.camera.core.processor.face.FaceFrameProcessor
import com.schaefer.camera.core.processor.face.FaceFrameProcessorImpl
import com.schaefer.camera.core.processor.luminosity.LuminosityFrameProcessor
import com.schaefer.camera.core.processor.luminosity.LuminosityFrameProcessorFactory
import com.schaefer.camera.di.CameraModule.application
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraContainer {
    fun provideContext(): Context {
        return application.applicationContext
    }

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
            VisionFaceDetector()
        )
    }
}
