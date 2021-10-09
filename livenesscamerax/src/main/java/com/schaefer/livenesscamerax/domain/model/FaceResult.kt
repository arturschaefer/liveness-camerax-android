package com.schaefer.livenesscamerax.domain.model

import android.graphics.Rect

internal data class FaceResult(
    val trackingId: Int?,
    val bounds: Rect,
    val headEulerAngleX: Float,
    val headEulerAngleY: Float,
    val headEulerAngleZ: Float,
    val smilingProbability: Float?,
    val rightEyeOpenProbability: Float?,
    val leftEyeOpenProbability: Float?,
    val luminosity: Float? = 0F
)
