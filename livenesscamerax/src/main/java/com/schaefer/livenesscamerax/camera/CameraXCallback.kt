package com.schaefer.livenesscamerax.camera

import java.io.File

interface CameraXCallback {
    fun onSuccess(photoFile: File)
    fun onError(exception: Exception)
}