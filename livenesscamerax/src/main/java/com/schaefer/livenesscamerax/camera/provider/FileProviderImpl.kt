package com.schaefer.livenesscamerax.camera.provider

import android.content.Context
import com.schaefer.livenesscamerax.R
import com.schaefer.livenesscamerax.domain.model.StorageType
import com.schaefer.livenesscamerax.presentation.model.CameraSettings
import org.apache.commons.io.FileUtils
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
private const val SUFFIX_PHOTO_FILE = ".jpg"
private const val DIR_NAME = "photos_liveness"

internal class FileProviderImpl(
    private val cameraSettings: CameraSettings,
    private val context: Context
) : FileProvider {

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

    private fun provideOutputDirectory(): File = when (cameraSettings.storageType) {
        StorageType.INTERNAL -> getInternalStorage()
        StorageType.EXTERNAL -> getExternalDirectory()
    }

    private fun getInternalStorage(): File {
        return context.getDir(DIR_NAME, Context.MODE_PRIVATE)
    }

    // TODO fix context.externalMediaDirs
    private fun getExternalDirectory(): File {
        val mediaDir = context.externalMediaDirs?.firstOrNull()?.let {
            File(it, context.getString(R.string.liveness_camerax_app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else context.filesDir
    }
}
