package com.schaefer.livenesscamerax.presentation.provider.result

import com.schaefer.livenesscamerax.presentation.model.PhotoResult

internal interface ResultHandler {
    fun success(photoResult: PhotoResult, filesPath: List<String>)
    fun error(exception: Exception)
}
