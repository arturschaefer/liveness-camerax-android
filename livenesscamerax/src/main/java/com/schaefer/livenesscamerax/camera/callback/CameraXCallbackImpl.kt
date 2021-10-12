package com.schaefer.livenesscamerax.camera.callback

import com.schaefer.livenesscamerax.presentation.model.PhotoResult
import java.io.File

internal class CameraXCallbackImpl(
    val onImageSavedAction: (PhotoResult) -> Unit,
    val onErrorAction: (Throwable) -> Unit
) : CameraXCallback {

    override fun onSuccess(photoFile: File) {
        onImageSavedAction(
            PhotoResult(
                createdAt = photoFile.name,
                filePath = photoFile.path
            )
        )
    }

    override fun onError(exception: Exception) {
        onErrorAction(exception)
    }
}
