package com.schaefer.domain.model

data class LivenessCameraXResultDomain(
    val createdByUser: PhotoResultDomain? = null,
    val createdBySteps: List<PhotoResultDomain>? = null,
    val error: LivenessCameraXErrorDomain? = null,
) {

    constructor(exception: Exception) : this(
        error = LivenessCameraXErrorDomain(
            message = exception.message.orEmpty(),
            cause = exception.cause.toString(),
            exception = exception
        )
    )
}
