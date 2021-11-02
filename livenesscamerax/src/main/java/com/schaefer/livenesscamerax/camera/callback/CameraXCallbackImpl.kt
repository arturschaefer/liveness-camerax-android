package com.schaefer.livenesscamerax.camera.callback

import com.schaefer.livenesscamerax.core.extensions.encoderFilePath
import com.schaefer.livenesscamerax.domain.usecase.editphoto.EditPhotoUseCase
import com.schaefer.livenesscamerax.presentation.model.PhotoResult
import java.io.File

internal class CameraXCallbackImpl(
    private val onImageSavedAction: (PhotoResult, Boolean) -> Unit,
    private val onErrorAction: (Exception) -> Unit,
    private val editPhotoUseCase: EditPhotoUseCase
) : CameraXCallback {

    override fun onSuccess(photoFile: File, takenByUser: Boolean) {
        editPhotoUseCase.editPhotoFile(photoFile)

        onImageSavedAction(
            PhotoResult(
                createdAt = photoFile.name,
                fileBase64 = photoFile.path.encoderFilePath()
            ),
            takenByUser
        )
    }

    override fun onError(exception: Exception) {
        onErrorAction(exception)
    }
}
