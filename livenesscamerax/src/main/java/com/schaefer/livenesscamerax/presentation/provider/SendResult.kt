package com.schaefer.livenesscamerax.presentation.provider

import com.schaefer.livenesscamerax.presentation.model.PhotoResult

internal interface SendResult {
    fun success(photoResult: PhotoResult, filesPath: List<String>)
    fun error(exception: Exception)
}
