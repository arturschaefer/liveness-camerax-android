package com.schaefer.livenesscamerax.di

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.schaefer.livenesscamerax.camera.processor.face.FaceFrameProcessor
import com.schaefer.livenesscamerax.camera.processor.face.FaceFrameProcessorImpl
import com.schaefer.livenesscamerax.camera.processor.luminosity.LuminosityFrameProcessor
import com.schaefer.livenesscamerax.camera.processor.luminosity.LuminosityFrameProcessorImpl
import com.schaefer.livenesscamerax.camera.provider.file.FileHandler
import com.schaefer.livenesscamerax.camera.provider.file.FileHandlerFactory
import com.schaefer.livenesscamerax.camera.provider.image.ImageHandler
import com.schaefer.livenesscamerax.camera.provider.image.ImageProviderFactory
import com.schaefer.livenesscamerax.domain.checker.LivenessChecker
import com.schaefer.livenesscamerax.domain.checker.LivenessCheckerFactory
import com.schaefer.livenesscamerax.domain.model.StorageType
import com.schaefer.livenesscamerax.presentation.navigation.LivenessEntryPoint
import com.schaefer.livenesscamerax.presentation.provider.resource.ResourcesProvider
import com.schaefer.livenesscamerax.presentation.provider.resource.ResourcesProviderFactory
import com.schaefer.livenesscamerax.presentation.provider.result.ResultHandler
import com.schaefer.livenesscamerax.presentation.provider.result.ResultHandlerFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

internal class LivenessCameraXContainer(private val application: Application) {

    fun resourceProvider(): ResourcesProvider {
        return ResourcesProviderFactory(application.applicationContext).create()
    }

    fun provideResultHandler(): ResultHandler {
        return ResultHandlerFactory().create()
    }

    fun provideLivenessChecker(): LivenessChecker {
        return LivenessCheckerFactory().create()
    }

    fun provideImageHandler(): ImageHandler {
        return ImageProviderFactory(application.applicationContext).create()
    }

    fun provideFileHandler(storageType: StorageType): FileHandler {
        return FileHandlerFactory(storageType, application.applicationContext).create()
    }

    fun provideCameraExecutor(): ExecutorService {
        return Executors.newSingleThreadExecutor()
    }

    @ExperimentalCoroutinesApi
    fun provideLuminosityFrameProcessor(): LuminosityFrameProcessor {
        return LuminosityFrameProcessorImpl()
    }

    @ExperimentalCoroutinesApi
    fun provideFaceFrameProcessor(lifecycleOwner: LifecycleOwner): FaceFrameProcessor {
        return FaceFrameProcessorImpl(lifecycleOwner.lifecycleScope)
    }

    val provideLivenessEntryPoint = LivenessEntryPoint
}
