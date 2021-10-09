package com.schaefer.livenesscamerax.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class LivenessType : Parcelable {
    LUMINOSITY,
    HEAD_FRONTAL,
    HEAD_LEFT,
    HEAD_RIGHT,
    HAS_SMILED,
    HAS_BLINKED,
}
