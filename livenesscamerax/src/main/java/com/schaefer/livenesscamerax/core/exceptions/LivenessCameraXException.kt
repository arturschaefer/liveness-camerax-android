package com.schaefer.livenesscamerax.core.exceptions

sealed class LivenessCameraXException : Exception() {

    data class StartCameraException(
        override val message: String?,
        override val cause: Throwable?
    ) : LivenessCameraXException()
}
