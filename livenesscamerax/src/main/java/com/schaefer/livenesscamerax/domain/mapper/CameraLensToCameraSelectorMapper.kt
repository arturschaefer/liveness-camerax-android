package com.schaefer.livenesscamerax.domain.mapper

import androidx.camera.core.CameraSelector
import com.schaefer.livenesscamerax.domain.model.CameraLens

class CameraLensToCameraSelectorMapper :
    com.schaefer.core.mapper.Mapper<CameraLens, CameraSelector> {

    override fun map(item: CameraLens): CameraSelector {
        return when (item) {
            CameraLens.DEFAULT_BACK_CAMERA -> CameraSelector.DEFAULT_BACK_CAMERA
            CameraLens.DEFAULT_FRONT_CAMERA -> CameraSelector.DEFAULT_FRONT_CAMERA
        }
    }
}
