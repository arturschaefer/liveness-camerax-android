package com.schaefer.livenesscamerax.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LivenessCameraXError(val message: String, val cause: String, val exception: Exception) :
    Parcelable
