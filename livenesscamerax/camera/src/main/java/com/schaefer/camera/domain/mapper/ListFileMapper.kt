package com.schaefer.camera.domain.mapper

import com.schaefer.core.extensions.encoderFilePath
import com.schaefer.core.extensions.getFileNameWithoutExtension
import com.schaefer.domain.model.PhotoResultDomain

internal fun List<String>.toPhotoResult(): List<PhotoResultDomain> {
    return this.map {
        PhotoResultDomain(
            createdAt = it.getFileNameWithoutExtension(),
            fileBase64 = it.encoderFilePath()
        )
    }
}
