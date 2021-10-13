package com.schaefer.livenesscamerax.camera.callback

import java.io.File

internal interface CameraXCallback {
    fun onSuccess(photoFile: File, takenByUser: Boolean)
    fun onError(exception: Exception)
}
