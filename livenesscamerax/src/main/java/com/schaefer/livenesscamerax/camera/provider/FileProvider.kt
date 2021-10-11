package com.schaefer.livenesscamerax.camera.provider

import android.content.Context
import com.schaefer.livenesscamerax.core.extensions.createStorageOutput
import com.schaefer.livenesscamerax.domain.model.CameraSettings
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
private const val SUFFIX_PHOTO_FILE = ".jpg"

internal class FileProvider(
    private val cameraSettings: CameraSettings,
    private val context: Context
) {
    fun getPhotoFile() = File(
        provideOutputDirectory(),
        provideSimpleDateFormatter().format(System.currentTimeMillis()) + SUFFIX_PHOTO_FILE
    )

    private fun provideSimpleDateFormatter() = SimpleDateFormat(
        FILENAME_FORMAT, Locale.US
    )

    private fun provideOutputDirectory() = cameraSettings.createStorageOutput(context)
}
