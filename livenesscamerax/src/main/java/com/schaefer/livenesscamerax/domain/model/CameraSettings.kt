package com.schaefer.livenesscamerax.domain.model

internal data class CameraSettings(
    val cameraLens: CameraLens = CameraLens.DEFAULT_FRONT_CAMERA,
    val captureQuality: CaptureQuality = CaptureQuality.CAPTURE_MODE_MAXIMIZE_QUALITY,
    val storageType: StorageType = StorageType.INTERNAL,
    val takeAutomaticPicture: Boolean = false,
    val isFlashEnabled: Boolean = false,
    val isZoomEnabled: Boolean = false,
    val isAutoFocusEnabled: Boolean = true
)
