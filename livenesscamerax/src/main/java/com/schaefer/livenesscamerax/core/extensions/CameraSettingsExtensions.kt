package com.schaefer.livenesscamerax.core.extensions

import androidx.camera.core.CameraSelector
import com.schaefer.livenesscamerax.domain.model.CameraLens
import com.schaefer.livenesscamerax.domain.model.CameraSettings

internal fun CameraSettings.getCameraSelector(): CameraSelector {
    return when (this.cameraLens) {
        CameraLens.DEFAULT_BACK_CAMERA -> CameraSelector.DEFAULT_BACK_CAMERA
        CameraLens.DEFAULT_FRONT_CAMERA -> CameraSelector.DEFAULT_FRONT_CAMERA
    }
}
