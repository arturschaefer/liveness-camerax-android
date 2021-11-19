package com.schaefer.domain.model

data class CameraSettingsDomain(
    val cameraLens: CameraLensDomain,
    val captureQuality: CaptureQualityDomain,
    val storageType: StorageTypeDomain,
    val takeAutomaticPicture: Boolean,
    val isFlashEnabled: Boolean,
    val isZoomEnabled: Boolean,
    val isAutoFocusEnabled: Boolean,
    val analyzeType: AnalyzeTypeDomain,
    val livenessStepList: List<StepLivenessDomain>
)
