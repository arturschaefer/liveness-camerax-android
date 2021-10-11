package com.schaefer.livenesscamerax.core.extensions

import android.content.Context
import androidx.camera.core.CameraSelector
import com.schaefer.livenesscamerax.R
import com.schaefer.livenesscamerax.domain.model.CameraLens
import com.schaefer.livenesscamerax.domain.model.CameraSettings
import com.schaefer.livenesscamerax.domain.model.StorageType
import java.io.File

private const val DIR_NAME = "photos_liveness"

internal fun CameraSettings.getCameraSelector(): CameraSelector {
    return when (this.cameraLens) {
        CameraLens.DEFAULT_BACK_CAMERA -> CameraSelector.DEFAULT_BACK_CAMERA
        CameraLens.DEFAULT_FRONT_CAMERA -> CameraSelector.DEFAULT_FRONT_CAMERA
    }
}

internal fun CameraSettings.createStorageOutput(context: Context): File? = when (this.storageType) {
    StorageType.INTERNAL -> context.getDir(DIR_NAME, Context.MODE_PRIVATE)
    StorageType.EXTERNAL -> getExternalDirectory(context)
}

private fun getExternalDirectory(context: Context): File {
    val mediaDir = context.externalMediaDirs?.firstOrNull()?.let {
        File(it, context.getString(R.string.liveness_camerax_app_name)).apply { mkdirs() }
    }
    return if (mediaDir != null && mediaDir.exists())
        mediaDir else context.filesDir
}
