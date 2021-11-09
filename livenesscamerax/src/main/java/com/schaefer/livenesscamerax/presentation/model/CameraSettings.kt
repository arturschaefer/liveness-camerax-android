package com.schaefer.livenesscamerax.presentation.model

import android.os.Parcelable
import com.schaefer.livenesscamerax.domain.model.AnalyzeType
import com.schaefer.livenesscamerax.domain.model.CameraLens
import com.schaefer.livenesscamerax.domain.model.CaptureQuality
import com.schaefer.livenesscamerax.domain.model.StepLiveness
import com.schaefer.livenesscamerax.domain.model.StorageType
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
