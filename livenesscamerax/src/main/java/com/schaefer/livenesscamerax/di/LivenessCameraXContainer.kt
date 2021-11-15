package com.schaefer.livenesscamerax.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.schaefer.camera.CameraX
import com.schaefer.camera.callback.CameraXCallback
import com.schaefer.camera.detector.VisionFaceDetector
import com.schaefer.camera.mapper.FaceToFaceResultMapper
import com.schaefer.camera.model.FaceResult
import com.schaefer.camera.processor.face.FaceFrameProcessor
import com.schaefer.camera.processor.face.FaceFrameProcessorImpl
import com.schaefer.camera.processor.luminosity.LuminosityFrameProcessor
import com.schaefer.camera.processor.luminosity.LuminosityFrameProcessorFactory
import com.schaefer.core.resourceprovider.ResourcesProvider
import com.schaefer.core.resourceprovider.ResourcesProviderFactory
import com.schaefer.domain.EditPhotoUseCase
import com.schaefer.domain.repository.CheckLivenessRepository
import com.schaefer.domain.repository.FileRepository
import com.schaefer.domain.repository.ResultLivenessRepository
import com.schaefer.livenesscamerax.camera.CameraXImpl
import com.schaefer.livenesscamerax.domain.mapper.CameraLensToCameraSelectorMapper
import com.schaefer.livenesscamerax.domain.model.StorageType
import com.schaefer.livenesscamerax.domain.repository.checkliveness.CheckLivenessRepositoryFactory
import com.schaefer.livenesscamerax.domain.repository.file.FileRepositoryFactory
import com.schaefer.livenesscamerax.domain.repository.resultliveness.ResultLivenessRepositoryFactory
import com.schaefer.livenesscamerax.domain.usecase.editphoto.EditPhotoUseCaseFactory
import com.schaefer.livenesscamerax.presentation.model.CameraSettings
import com.schaefer.livenesscamerax.presentation.model.PhotoResult
import com.schaefer.livenesscamerax.presentation.navigation.LivenessEntryPoint
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

internal class LivenessCameraXContainer(private val application: Application) {
    val provideLivenessEntryPoint = LivenessEntryPoint

    fun provideContext(): Context {
        return application.applicationContext
    }

    fun provideResourceProvider(): ResourcesProvider {
        return ResourcesProviderFactory(provideContext()).create()
    }

    fun provideResultLivenessRepository(): ResultLivenessRepository<PhotoResult> {
        return ResultLivenessRepositoryFactory.create()
    }

    fun provideCheckLivenessRepository(): CheckLivenessRepository<FaceResult> {
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
