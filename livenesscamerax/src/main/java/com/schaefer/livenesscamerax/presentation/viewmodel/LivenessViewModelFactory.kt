package com.schaefer.livenesscamerax.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.schaefer.livenesscamerax.presentation.provider.ResourceProvider
import kotlinx.coroutines.InternalCoroutinesApi

//Reference https://github.com/android/architecture-components-samples/blob/master/BasicRxJavaSampleKotlin/app/src/main/java/com/example/android/observability/ui/ViewModelFactory.kt

private const val UNKNOWN_VIEW_MODEL = "Unknown ViewModel class"

@InternalCoroutinesApi
internal class LivenessViewModelFactory(private val resourceProvider: ResourceProvider) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LivenessViewModel::class.java)) {
            return LivenessViewModel(resourceProvider) as T
        }
        throw IllegalArgumentException(UNKNOWN_VIEW_MODEL)
    }
}