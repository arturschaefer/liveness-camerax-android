package com.schaefer.domain.repository

import com.schaefer.domain.model.HeadMovement

interface CheckLivenessRepository<FACE> {
    fun isEyeOpened(eyeOpenedProbabilityValue: Float?): Boolean

    fun checkBothEyes(
        leftEyeProbability: Float?,
        rightEyeProbability: Float?,
        callbackBlinked: (Boolean) -> Unit
    ): Boolean

    fun hasMoreThanOneFace(listFaceResult: List<FACE>): Boolean

    fun checkSmile(smilingProbability: Float?, callbackSmiled: (Boolean) -> Unit): Boolean

    fun detectEulerYMovement(
        headEulerAngleY: Float
    ): HeadMovement

    fun validateHeadMovement(
        face: FACE,
        headMovement: HeadMovement,
        removeCurrentStep: (Boolean) -> Unit
    )

    fun validateAtLeastOneEyeIsOpen(face: FACE): Boolean
}
