package com.schaefer.domain.repository

import com.schaefer.domain.model.HeadMovement

interface LivenessRepository<FACE> {
    fun isEyeOpened(eyeOpenedProbabilityValue: Float?): Boolean

    fun validateBlinkedEyes(
        leftEyeProbability: Float?,
        rightEyeProbability: Float?,
        callbackBlinked: (Boolean) -> Unit
    ): Boolean

    fun isFacesDetectedCorrect(listFaceResult: List<FACE>): Boolean

    fun checkSmile(smilingProbability: Float?, callbackSmiled: (Boolean) -> Unit)

    fun detectEulerYMovement(
        headEulerAngleY: Float
    ): HeadMovement

    fun validateHeadMovement(
        face: FACE,
        headMovement: HeadMovement,
        callbackHeadMovement: (Boolean) -> Unit
    )
}
