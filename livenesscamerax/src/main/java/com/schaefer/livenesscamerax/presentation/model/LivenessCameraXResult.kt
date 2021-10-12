package com.schaefer.livenesscamerax.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LivenessCameraXResult(
    val createdByUser: PhotoResult? = null,
    val createdBySteps: List<PhotoResult>? = null,
    val error: LivenessCameraXError? = null,
): Parcelable

