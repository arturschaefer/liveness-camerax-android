package com.schaefer.livenesscamerax.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.schaefer.livenesscamerax.domain.logic.LivenessChecker
import com.schaefer.livenesscamerax.presentation.provider.ResourcesProvider
import kotlinx.coroutines.InternalCoroutinesApi

// Reference https://github.com/android/architecture-components-samples/blob/master/BasicRxJavaSampleKotlin/app/src/main/java/com/example/android/observability/ui/ViewModelFactory.kt

private const val UNKNOWN_VIEW_MODEL = "Unknown ViewModel class"

@InternalCoroutinesApi
internal class LivenessViewModelFactory(
    private val resourcesProvider: ResourcesProvider,
    private val livenessChecker: LivenessChecker
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LivenessViewModel::class.java)) {
            return LivenessViewModel(resourcesProvider, livenessChecker) as T
        }
        throw IllegalArgumentException(UNKNOWN_VIEW_MODEL)
    }
}
