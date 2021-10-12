package com.schaefer.livenesscamerax.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoResult(val createdAt: String, val filePath: String) : Parcelable