package com.schaefer.livenesscamerax.domain.model

import android.os.Parcelable
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
