package com.schaefer.livenesscamerax.domain.usecase.editphoto

import java.io.File

internal interface EditPhotoUseCase {
    fun editPhotoFile(photoFile: File)
}
