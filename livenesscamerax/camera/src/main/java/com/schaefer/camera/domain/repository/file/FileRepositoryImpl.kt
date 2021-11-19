package com.schaefer.camera.domain.repository.file

import android.content.Context
import com.schaefer.domain.model.StorageTypeDomain
import com.schaefer.domain.repository.FileRepository
import org.apache.commons.io.FileUtils
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
private const val SUFFIX_PHOTO_FILE = ".jpg"
private const val DIR_NAME = "photos_liveness"
private const val APP_NAME = "LivenessCameraX"

internal class FileRepositoryImpl(
    private val storageType: StorageTypeDomain,
    private val context: Context
) : FileRepository {

    init {
        deleteStorageFiles()
    }

    override fun getPhotoFile() = File(
        provideOutputDirectory(),
        provideSimpleDateFormatter().format(System.currentTimeMillis()) + SUFFIX_PHOTO_FILE
    )

    override fun deleteStorageFiles(): Boolean {
        return runCatching {
            FileUtils.deleteDirectory(getExternalDirectory())
            FileUtils.deleteDirectory(getInternalStorage())
        }.isSuccess
    }

    override fun getPathOfAllPhotos(): List<String> {
        return File(provideOutputDirectory().path).walkTopDown()
            .filter { it.isDirectory.not() }
            .map { it.path }
            .toList()
    }

    private fun provideSimpleDateFormatter() = SimpleDateFormat(FILENAME_FORMAT, Locale.US)

    private fun provideOutputDirectory(): File = when (storageType) {
        StorageTypeDomain.INTERNAL -> getInternalStorage()
        StorageTypeDomain.EXTERNAL -> getExternalDirectory()
    }

    private fun getInternalStorage(): File {
        return context.getDir(DIR_NAME, Context.MODE_PRIVATE)
    }

    private fun getExternalDirectory(): File {
        val mediaDir = context.filesDir.let {
            File(it, APP_NAME).apply { mkdirs() }
        }
        return if (mediaDir.exists())
            mediaDir else context.filesDir
    }
}
