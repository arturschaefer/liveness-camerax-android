package com.schaefer.camera.mapper

import com.google.mlkit.vision.face.Face
import com.schaefer.camera.model.FaceResult

class FaceToFaceResultMapper : com.schaefer.core.mapper.Mapper<Face, FaceResult> {

    override fun map(item: Face): FaceResult {
        return FaceResult(
            trackingId = item.trackingId,
            bounds = item.boundingBox,
            headEulerAngleX = item.headEulerAngleX,
            headEulerAngleY = item.headEulerAngleY,
            headEulerAngleZ = item.headEulerAngleZ,
            smilingProbability = item.smilingProbability,
            rightEyeOpenProbability = item.rightEyeOpenProbability,
            leftEyeOpenProbability = item.leftEyeOpenProbability,
        )
    }
}
