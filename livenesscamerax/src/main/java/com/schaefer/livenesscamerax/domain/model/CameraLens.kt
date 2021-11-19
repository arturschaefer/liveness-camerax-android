package com.schaefer.livenesscamerax.domain.model

import com.schaefer.domain.model.CameraLensDomain

enum class CameraLens {
    DEFAULT_BACK_CAMERA,
    DEFAULT_FRONT_CAMERA
}

fun CameraLens.toDomain(): CameraLensDomain {
    return when (this) {
        CameraLens.DEFAULT_BACK_CAMERA -> CameraLensDomain.DEFAULT_BACK_CAMERA
        CameraLens.DEFAULT_FRONT_CAMERA -> CameraLensDomain.DEFAULT_FRONT_CAMERA
    }
}
