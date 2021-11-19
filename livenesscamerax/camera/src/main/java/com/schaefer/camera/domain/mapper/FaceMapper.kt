package com.schaefer.camera.domain.mapper

import com.google.mlkit.vision.face.Face
import com.schaefer.camera.domain.model.FaceResult

internal fun Face.toFaceResult(): FaceResult {
    return FaceResult(
        trackingId = this.trackingId,
        bounds = this.boundingBox,
        headEulerAngleX = this.headEulerAngleX,
        headEulerAngleY = this.headEulerAngleY,
        headEulerAngleZ = this.headEulerAngleZ,
        smilingProbability = this.smilingProbability,
        rightEyeOpenProbability = this.rightEyeOpenProbability,
        leftEyeOpenProbability = this.leftEyeOpenProbability,
    )
}
