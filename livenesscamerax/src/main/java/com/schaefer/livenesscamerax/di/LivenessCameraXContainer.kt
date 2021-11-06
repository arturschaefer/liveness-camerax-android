package com.schaefer.livenesscamerax.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.schaefer.livenesscamerax.camera.CameraX
import com.schaefer.livenesscamerax.camera.CameraXImpl
import com.schaefer.livenesscamerax.camera.callback.CameraXCallback
import com.schaefer.livenesscamerax.camera.detector.VisionFaceDetector
import com.schaefer.livenesscamerax.camera.processor.face.FaceFrameProcessor
import com.schaefer.livenesscamerax.camera.processor.face.FaceFrameProcessorImpl
import com.schaefer.livenesscamerax.camera.processor.luminosity.LuminosityFrameProcessor
import com.schaefer.livenesscamerax.camera.processor.luminosity.LuminosityFrameProcessorImpl
import com.schaefer.livenesscamerax.core.resourceprovider.ResourcesProvider
import com.schaefer.livenesscamerax.core.resourceprovider.ResourcesProviderFactory
import com.schaefer.livenesscamerax.domain.mapper.CameraLensToCameraSelectorMapper
import com.schaefer.livenesscamerax.domain.mapper.FaceToFaceResultMapper
import com.schaefer.livenesscamerax.domain.model.StorageType
import com.schaefer.livenesscamerax.domain.repository.checkliveness.CheckLivenessRepository
import com.schaefer.livenesscamerax.domain.repository.checkliveness.CheckLivenessRepositoryFactory
import com.schaefer.livenesscamerax.domain.repository.file.FileRepository
import com.schaefer.livenesscamerax.domain.repository.file.FileRepositoryFactory
import com.schaefer.livenesscamerax.domain.repository.resultliveness.ResultLivenessRepository
import com.schaefer.livenesscamerax.domain.repository.resultliveness.ResultLivenessRepositoryFactory
import com.schaefer.livenesscamerax.domain.usecase.editphoto.EditPhotoUseCase
import com.schaefer.livenesscamerax.domain.usecase.editphoto.EditPhotoUseCaseFactory
import com.schaefer.livenesscamerax.presentation.model.CameraSettings
import com.schaefer.livenesscamerax.presentation.navigation.LivenessEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

internal class LivenessCameraXContainer(private val application: Application) {
    val provideLivenessEntryPoint = LivenessEntryPoint

    fun provideContext(): Context {
        return application.applicationContext
    }

    fun provideResourceProvider(): ResourcesProvider {
        return ResourcesProviderFactory.create()
    }

    fun provideResultLivenessRepository(): ResultLivenessRepository {
        return ResultLivenessRepositoryFactory.create()
    }

    fun provideCheckLivenessRepository(): CheckLivenessRepository {
        return CheckLivenessRepositoryFactory.create()
    }

    fun provideEditPhotoUseCase(): EditPhotoUseCase {
        return EditPhotoUseCaseFactory.create()
    }

    private fun provideFileRepository(storageType: StorageType): FileRepository {
        return FileRepositoryFactory.apply {
            this.storageType = storageType
        }.create()
    }

    fun provideExecutorService(): ExecutorService {
        return Executors.newSingleThreadExecutor()
    }

    @ExperimentalCoroutinesApi
    fun provideLuminosityFrameProcessor(): LuminosityFrameProcessor {
        return LuminosityFrameProcessorImpl()
    }

    @ExperimentalCoroutinesApi
    fun provideFaceFrameProcessor(lifecycleOwner: LifecycleOwner): FaceFrameProcessor {
        return FaceFrameProcessorImpl(
            lifecycleOwner.lifecycleScope,
            FaceToFaceResultMapper(),
            VisionFaceDetector()
        )
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun provideCameraX(
        cameraSettings: CameraSettings,
        cameraXCallback: CameraXCallback,
        lifecycleOwner: LifecycleOwner
    ): CameraX {
        return CameraXImpl(
            settings = cameraSettings,
            cameraXCallback = cameraXCallback,
            lifecycleOwner = lifecycleOwner,
            cameraLensToCameraSelectorMapper = CameraLensToCameraSelectorMapper(),
            fileRepository = provideFileRepository(cameraSettings.storageType)
        )
    }
}
