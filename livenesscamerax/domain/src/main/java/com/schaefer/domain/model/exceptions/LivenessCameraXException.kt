package com.schaefer.domain.model.exceptions

sealed class LivenessCameraXException : Exception() {

    data class StartCameraException(
        override val message: String?,
        override val cause: Throwable?
    ) : LivenessCameraXException()

    data class ContextSwitchException(
        override val message: String? = ErrorsTypes.CONTEXT_SWITCHED.name
    ) : LivenessCameraXException()

    data class UserCanceledException(
        override val message: String? = ErrorsTypes.USER_CANCELED.name
    ) : LivenessCameraXException()

    data class PermissionDenied(
        override val message: String? = ErrorsTypes.PERMISSION_DENIED.name
    ) : LivenessCameraXException()

    data class PermissionUnknown(
        override val message: String? = ErrorsTypes.PERMISSION_UNKNOWN.name
    ) : LivenessCameraXException()
}
