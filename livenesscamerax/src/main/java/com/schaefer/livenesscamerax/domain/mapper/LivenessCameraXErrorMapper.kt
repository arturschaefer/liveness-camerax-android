package com.schaefer.livenesscamerax.domain.mapper

import com.schaefer.domain.model.LivenessCameraXErrorDomain
import com.schaefer.livenesscamerax.domain.model.LivenessCameraXError

fun LivenessCameraXErrorDomain.toPresentation(): LivenessCameraXError {
    return LivenessCameraXError(
        message = this.message,
        cause = this.cause,
        exception = this.exception
    )
}
