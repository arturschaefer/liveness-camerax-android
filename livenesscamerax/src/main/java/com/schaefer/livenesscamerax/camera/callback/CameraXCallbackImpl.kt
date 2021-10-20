package com.schaefer.livenesscamerax.camera.callback

import com.schaefer.livenesscamerax.camera.provider.ImageProvider
import com.schaefer.livenesscamerax.presentation.model.PhotoResult
import java.io.File

internal class CameraXCallbackImpl(
    val onImageSavedAction: (PhotoResult, Boolean) -> Unit,
    val onErrorAction: (Exception) -> Unit,
    private val imageProvider: ImageProvider
) : CameraXCallback {

    override fun onSuccess(photoFile: File, takenByUser: Boolean) {
        imageProvider.editPhotoFile(photoFile)

        onImageSavedAction(
            PhotoResult(
                createdAt = photoFile.name,
                filePath = photoFile.path
            ),
            takenByUser
        )
    }

    override fun onError(exception: Exception) {
        onErrorAction(exception)
    }
}
