package com.schaefer.livenesscamerax.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.schaefer.camera.domain.model.FaceResult
import com.schaefer.core.resourceprovider.ResourcesProvider
import com.schaefer.domain.repository.LivenessRepository
import com.schaefer.livenesscamerax.di.LibraryModule.container
import com.schaefer.livenesscamerax.domain.usecase.GetStepMessageUseCase

private const val UNKNOWN_VIEW_MODEL = "Unknown ViewModel class"

internal class LivenessViewModelFactory(
    private val resourcesProvider: ResourcesProvider = container.provideResourceProvider(),
    private val livenessRepository: LivenessRepository<FaceResult> = container.provideLivenessRepository(),
    private val getStepMessageUseCase: GetStepMessageUseCase = container.provideGetStepMessagesUseCase()
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LivenessViewModel::class.java)) {
            return LivenessViewModel(
                resourcesProvider,
                livenessRepository,
                getStepMessageUseCase
            ) as T
        }
        throw IllegalArgumentException(UNKNOWN_VIEW_MODEL)
    }
}
