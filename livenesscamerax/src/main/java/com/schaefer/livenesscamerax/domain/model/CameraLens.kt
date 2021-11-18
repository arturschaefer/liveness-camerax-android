package com.schaefer.livenesscamerax.domain.model

import androidx.camera.core.CameraSelector
import com.schaefer.domain.model.CameraLensDomain

enum class CameraLens {
    DEFAULT_BACK_CAMERA,
    DEFAULT_FRONT_CAMERA
}

fun CameraLens.toCameraSelector(): CameraSelector {
    return when (this) {
        CameraLens.DEFAULT_BACK_CAMERA -> CameraSelector.DEFAULT_BACK_CAMERA
        CameraLens.DEFAULT_FRONT_CAMERA -> CameraSelector.DEFAULT_FRONT_CAMERA
    }
}

fun CameraLens.toDomain(): CameraLensDomain {
    return when (this) {
        CameraLens.DEFAULT_BACK_CAMERA -> CameraLensDomain.DEFAULT_BACK_CAMERA
        CameraLens.DEFAULT_FRONT_CAMERA -> CameraLensDomain.DEFAULT_FRONT_CAMERA
    }
}
