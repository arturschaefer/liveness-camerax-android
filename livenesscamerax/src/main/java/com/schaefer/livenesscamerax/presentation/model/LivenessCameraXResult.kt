package com.schaefer.livenesscamerax.presentation.model

import android.os.Parcelable
import com.schaefer.core.extensions.encoderFilePath
import com.schaefer.core.extensions.getFileNameWithoutExtension
import kotlinx.parcelize.Parcelize

@Parcelize
data class LivenessCameraXResult(
    val createdByUser: PhotoResult? = null,
    val createdBySteps: List<PhotoResult>? = null,
    val error: LivenessCameraXError? = null,
) : Parcelable {

    constructor(
        photoResult: PhotoResult,
        filesPath: List<String>,
    ) : this(
        createdByUser = photoResult,
        createdBySteps = filesPath.map { path ->
            PhotoResult(
                createdAt = path.getFileNameWithoutExtension(),
                fileBase64 = path.encoderFilePath()
            )
        }
    )

    constructor(exception: Exception) : this(
        error = LivenessCameraXError(
            message = exception.message.orEmpty(),
            cause = exception.cause.toString(),
            exception = exception
        )
    )
}
