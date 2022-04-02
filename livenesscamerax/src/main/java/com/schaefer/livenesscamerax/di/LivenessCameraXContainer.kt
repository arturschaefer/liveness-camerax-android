package com.schaefer.livenesscamerax.di

import android.app.Application
import android.content.Context
import com.schaefer.camera.domain.model.FaceResult
import com.schaefer.camera.domain.repository.checkliveness.CheckLivenessRepositoryFactory
import com.schaefer.camera.domain.repository.resultliveness.ResultLivenessRepositoryFactory
import com.schaefer.core.resourceprovider.ResourcesProvider
import com.schaefer.core.resourceprovider.ResourcesProviderFactory
import com.schaefer.domain.model.PhotoResultDomain
import com.schaefer.domain.repository.LivenessRepository
import com.schaefer.domain.repository.ResultLivenessRepository
import com.schaefer.livenesscamerax.domain.usecase.GetStepMessageUseCase
import com.schaefer.livenesscamerax.navigation.LivenessEntryPoint

internal class LivenessCameraXContainer(private val application: Application) {
    private val provideLivenessEntryPoint = LivenessEntryPoint

    private fun provideContext(): Context {
        return application.applicationContext
    }

    fun provideResourceProvider(): ResourcesProvider {
        return ResourcesProviderFactory(provideContext()).create()
    }

    fun provideResultLivenessRepository(): ResultLivenessRepository<PhotoResultDomain> {
        return ResultLivenessRepositoryFactory.apply {
            resultCallback = provideLivenessEntryPoint::postResultCallback
        }.create()
    }

    fun provideLivenessRepository(): LivenessRepository<FaceResult> {
        return CheckLivenessRepositoryFactory.create()
    }

    fun provideGetStepMessagesUseCase(
        resourcesProvider: ResourcesProvider = provideResourceProvider()
    ): GetStepMessageUseCase {
        return GetStepMessageUseCase(resourcesProvider)
    }
}
