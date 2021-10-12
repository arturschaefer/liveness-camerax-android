package com.schaefer.livenesscamerax.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class StepLiveness : Parcelable {
    STEP_LUMINOSITY,
    STEP_HEAD_FRONTAL,
    STEP_HEAD_LEFT,
    STEP_HEAD_RIGHT,
    STEP_SMILE,
    STEP_BLINK,
}
