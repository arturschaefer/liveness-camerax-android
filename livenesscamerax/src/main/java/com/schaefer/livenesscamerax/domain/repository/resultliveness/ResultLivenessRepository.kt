package com.schaefer.livenesscamerax.domain.repository.resultliveness

import com.schaefer.livenesscamerax.presentation.model.PhotoResult

internal interface ResultLivenessRepository {
    fun success(photoResult: PhotoResult, filesPath: List<String>)
    fun error(exception: Exception)
}
