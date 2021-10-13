package com.schaefer.livenesscamerax.presentation.provider

import com.schaefer.livenesscamerax.presentation.model.LivenessCameraXResult

interface SendResult {
    fun success(resultSuccess: LivenessCameraXResult)
    fun error(exception: Exception)
}
