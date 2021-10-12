package com.schaefer.livenesscamerax.presentation.viewmodel

import com.schaefer.livenesscamerax.core.viewmodel.UIAction
import com.schaefer.livenesscamerax.presentation.model.PhotoResult

internal sealed class LivenessAction : UIAction {

    data class LivenessCompleted(val photoResult: PhotoResult) : LivenessAction()

    object LivenessCanceled : LivenessAction()

    data class TakePicture(val typeOfPicture: String) : LivenessAction()
}
