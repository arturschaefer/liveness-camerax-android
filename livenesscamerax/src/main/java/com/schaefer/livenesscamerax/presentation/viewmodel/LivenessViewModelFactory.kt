package com.schaefer.livenesscamerax.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.schaefer.camera.model.FaceResult
import com.schaefer.core.resourceprovider.ResourcesProvider
import com.schaefer.domain.repository.CheckLivenessRepository
import kotlinx.coroutines.InternalCoroutinesApi

// Reference https://github.com/android/architecture-components-samples/blob/master/BasicRxJavaSampleKotlin/app/src/main/java/com/example/android/observability/ui/ViewModelFactory.kt

private const val UNKNOWN_VIEW_MODEL = "Unknown ViewModel class"

@InternalCoroutinesApi
internal class LivenessViewModelFactory(
    private val resourcesProvider: ResourcesProvider,
    private val checkLivenessRepository: CheckLivenessRepository<FaceResult>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LivenessViewModel::class.java)) {
            return LivenessViewModel(resourcesProvider, checkLivenessRepository) as T
        }
        throw IllegalArgumentException(UNKNOWN_VIEW_MODEL)
    }
}
