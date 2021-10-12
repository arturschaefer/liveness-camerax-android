package com.schaefer.livenesscamerax.camera.provider

import java.io.File

interface FileProvider {
    fun getPhotoFile(): File
    fun deleteStorageFiles(): Boolean
    fun getPathOfAllPhotos(): List<String>
}