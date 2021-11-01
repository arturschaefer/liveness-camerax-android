package com.schaefer.livenesscamerax.domain.repository.file

import java.io.File

internal interface FileRepository {
    fun getPhotoFile(): File
    fun deleteStorageFiles(): Boolean
    fun getPathOfAllPhotos(): List<String>
}
