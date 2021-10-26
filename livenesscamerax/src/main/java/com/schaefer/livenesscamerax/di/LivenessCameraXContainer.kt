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
import com.schaefer.livenesscamerax.presentation.provider.resource.ResourcesProvider
import com.schaefer.livenesscamerax.presentation.provider.resource.ResourcesProviderFactory
import com.schaefer.livenesscamerax.presentation.provider.result.ResultHandler
import com.schaefer.livenesscamerax.presentation.provider.result.ResultProviderFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

internal class LivenessCameraXContainer(private val application: Application) {

    val resourceProvider: ResourcesProvider by lazy {
        ResourcesProviderFactory(application.applicationContext).create()
    }

    val provideResultHandler: ResultHandler by lazy {
        ResultProviderFactory().create()
    }

    val provideLivenessChecker: LivenessChecker by lazy {
        LivenessCheckerFactory().create()
    }

    val provideImageHandler: ImageHandler by lazy {
        ImageProviderFactory(application.applicationContext).create()
    }

    fun provideFileHandler(storageType: StorageType): FileHandler =
        FileHandlerFactory(storageType, application.applicationContext).create()

    val provideSingleThreadExecutor: ExecutorService by lazy { Executors.newSingleThreadExecutor() }

    @ExperimentalCoroutinesApi
    val provideLuminosityFrameProcessor: LuminosityFrameProcessor by lazy {
        LuminosityFrameProcessorImpl()
    }

    @ExperimentalCoroutinesApi
    fun provideFaceFrameProcessor(lifecycleOwner: LifecycleOwner): FaceFrameProcessor {
        return FaceFrameProcessorImpl(lifecycleOwner.lifecycleScope)
    }
}
