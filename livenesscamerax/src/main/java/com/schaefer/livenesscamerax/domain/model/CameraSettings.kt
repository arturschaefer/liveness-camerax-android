package com.schaefer.livenesscamerax.domain.model

import android.os.Parcelable
import com.schaefer.domain.model.CameraSettingsDomain
import kotlinx.parcelize.Parcelize

@Parcelize
data class CameraSettings(
    val cameraLens: CameraLens = CameraLens.DEFAULT_FRONT_CAMERA,
    val captureQuality: CaptureQuality = CaptureQuality.CAPTURE_MODE_MAXIMIZE_QUALITY,
    val storageType: StorageType = StorageType.INTERNAL,
    val takeAutomaticPicture: Boolean = false,
    val isFlashEnabled: Boolean = false,
    val isZoomEnabled: Boolean = false,
    val isAutoFocusEnabled: Boolean = true,
    val analyzeType: AnalyzeType = AnalyzeType.FACE_PROCESSOR,
    val livenessStepList: ArrayList<StepLiveness> = arrayListOf(
        StepLiveness.STEP_LUMINOSITY,
        StepLiveness.STEP_SMILE,
        StepLiveness.STEP_BLINK,
    )
) : Parcelable

internal fun CameraSettings.toDomain(): CameraSettingsDomain {
    return CameraSettingsDomain(
        cameraLens = this.cameraLens.toDomain(),
        captureQuality = this.captureQuality.toDomain(),
        storageType = this.storageType.toDomain(),
        takeAutomaticPicture = this.takeAutomaticPicture,
        isFlashEnabled = this.isFlashEnabled,
        isZoomEnabled = this.isZoomEnabled,
        isAutoFocusEnabled = this.isAutoFocusEnabled,
        analyzeType = this.analyzeType.toDomain(),
        livenessStepList = this.livenessStepList.map { it.toDomain() }
    )
}
