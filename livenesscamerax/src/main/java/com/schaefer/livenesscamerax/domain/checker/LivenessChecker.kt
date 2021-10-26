package com.schaefer.livenesscamerax.domain.checker

import com.schaefer.livenesscamerax.domain.model.FaceResult
import com.schaefer.livenesscamerax.domain.model.HeadMovement

internal interface LivenessChecker {
    fun isEyeOpened(eyeOpenedProbabilityValue: Float?): Boolean

    fun checkBothEyes(
        leftEyeProbability: Float?,
        rightEyeProbability: Float?,
        callbackBlinked: (Boolean) -> Unit
    ): Boolean

    fun hasMoreThanOneFace(listFaceResult: List<FaceResult>): Boolean

    fun checkSmile(smilingProbability: Float?, callbackSmiled: (Boolean) -> Unit): Boolean

    fun detectEulerYMovement(
        headEulerAngleY: Float
    ): HeadMovement

    fun validateHeadMovement(
        face: FaceResult,
        headMovement: HeadMovement,
        removeCurrentStep: (Boolean) -> Unit
    )

    fun validateAtLeastOneEyeIsOpen(face: FaceResult): Boolean
}
