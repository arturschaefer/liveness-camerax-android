package com.schaefer.domain.repository

import java.io.File

interface FileRepository {
    fun getPhotoFile(): File
    fun deleteStorageFiles(): Boolean
    fun getPathOfAllPhotos(): List<String>
}
