package com.schaefer.livenesscamerax.domain.model

data class LivenessCameraXError(
    val message: String,
    val cause: String,
    val exception: Exception
)
