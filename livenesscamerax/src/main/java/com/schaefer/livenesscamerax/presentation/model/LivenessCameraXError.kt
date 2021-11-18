package com.schaefer.livenesscamerax.presentation.model

data class LivenessCameraXError(
    val message: String,
    val cause: String,
    val exception: Exception
)
