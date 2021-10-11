package com.schaefer.livenesscamerax.presentation.viewmodel

import com.schaefer.livenesscamerax.core.viewmodel.UIAction

internal sealed class LivenessAction : UIAction {
    object LivenessCompleted : LivenessAction()
    object LivenessCanceled : LivenessAction()
    data class EnableCameraButton(val enableButton: Boolean) : LivenessAction()
}
