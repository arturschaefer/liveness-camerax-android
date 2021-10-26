package com.schaefer.livenesscamerax.camera.provider.file

import java.io.File

internal interface FileHandler {
    fun getPhotoFile(): File
    fun deleteStorageFiles(): Boolean
    fun getPathOfAllPhotos(): List<String>
}
