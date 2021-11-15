package com.schaefer.camera.model

import android.graphics.Rect

data class FaceResult(
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
