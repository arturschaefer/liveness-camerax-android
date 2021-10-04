package com.schaefer.livenesscamerax.camera

import android.content.Context
import java.io.File

class CameraXCallbackImpl(
    val onImageSavedAction: (File) -> Unit,
    val onErrorAction: (Throwable) -> Unit,
    val context: Context,
) : CameraXCallback {

    override fun onSuccess(photoFile: File) {
        onImageSavedAction(
            photoFile
        )
    }

    override fun onError(exception: Exception) {
        onErrorAction(exception)
    }
}