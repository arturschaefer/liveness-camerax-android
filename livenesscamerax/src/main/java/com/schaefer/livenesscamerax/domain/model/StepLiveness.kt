package com.schaefer.livenesscamerax.domain.model

import android.os.Parcelable
import com.schaefer.domain.model.StepLivenessDomain
import kotlinx.parcelize.Parcelize

@Parcelize
enum class StepLiveness(val textValue: String) : Parcelable {
    STEP_LUMINOSITY("Luminosity"),
    STEP_HEAD_FRONTAL("Head Stay Frontal"),
    STEP_HEAD_LEFT("Head Stay Frontal"),
    STEP_HEAD_RIGHT("Head Movement Frontal"),
    STEP_SMILE("Smile"),
    STEP_BLINK("Blink"),
}

fun StepLiveness.toDomain(): StepLivenessDomain {
    return when (this) {
        StepLiveness.STEP_LUMINOSITY -> StepLivenessDomain.STEP_LUMINOSITY
        StepLiveness.STEP_HEAD_FRONTAL -> StepLivenessDomain.STEP_HEAD_FRONTAL
        StepLiveness.STEP_HEAD_LEFT -> StepLivenessDomain.STEP_HEAD_LEFT
        StepLiveness.STEP_HEAD_RIGHT -> StepLivenessDomain.STEP_HEAD_RIGHT
        StepLiveness.STEP_SMILE -> StepLivenessDomain.STEP_SMILE
        StepLiveness.STEP_BLINK -> StepLivenessDomain.STEP_BLINK
    }
}
