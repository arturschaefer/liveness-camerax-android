package com.schaefer.camera.di

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.schaefer.camera.CameraX
import com.schaefer.camera.CameraXImpl
import com.schaefer.camera.core.callback.CameraXCallback
import com.schaefer.camera.core.detector.VisionFaceDetector
import com.schaefer.camera.core.processor.face.FaceFrameProcessor
import com.schaefer.camera.core.processor.face.FaceFrameProcessorFactory
import com.schaefer.camera.core.processor.luminosity.LuminosityFrameProcessor
import com.schaefer.camera.core.processor.luminosity.LuminosityFrameProcessorFactory
import com.schaefer.camera.di.CameraModule.application
import com.schaefer.camera.domain.repository.file.FileRepositoryFactory
import com.schaefer.camera.domain.usecase.EditPhotoUseCaseFactory
import com.schaefer.domain.EditPhotoUseCase
import com.schaefer.domain.model.CameraSettingsDomain
import com.schaefer.domain.model.StorageTypeDomain
import com.schaefer.domain.repository.FileRepository
import kotlinx.coroutines.CoroutineScope
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

internal class CameraContainer {
    fun provideContext(): Context {
        return application.applicationContext
    }

    fun provideCoroutineScope(): CoroutineScope {
        return CameraModule.lifecycleOwner.lifecycleScope
    }

    fun provideExecutorService(): ExecutorService {
        return Executors.newSingleThreadExecutor()
    }

    fun provideLuminosityFrameProcessor(): LuminosityFrameProcessor {
        return LuminosityFrameProcessorFactory.create()
    }

    fun provideFaceFrameProcessor(): FaceFrameProcessor {
        return FaceFrameProcessorFactory.create()
    }

    fun provideVisionFaceDetector(): VisionFaceDetector {
        return VisionFaceDetector()
    }

    fun provideEditPhotoUseCase(): EditPhotoUseCase {
        return EditPhotoUseCaseFactory.create()
    }

    private fun provideFileRepository(storageType: StorageTypeDomain): FileRepository {
        return FileRepositoryFactory.apply { this.storageType = storageType }.create()
    }

    fun provideCameraX(
        cameraSettings: CameraSettingsDomain,
        cameraXCallback: CameraXCallback,
        lifecycleOwner: LifecycleOwner
    ): CameraX {
        return CameraXImpl(
            settings = cameraSettings,
            cameraXCallback = cameraXCallback,
            lifecycleOwner = lifecycleOwner,
            fileRepository = provideFileRepository(cameraSettings.storageType)
        )
    }
}
