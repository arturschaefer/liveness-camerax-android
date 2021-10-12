package com.schaefer.livenesscamerax.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LivenessCameraXResult(
    val createdByUser: PhotoResult,
    val createdBySteps: List<PhotoResult>
): Parcelable
