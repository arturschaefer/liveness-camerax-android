package com.schaefer.camera.domain.mapper

import androidx.camera.core.CameraSelector
import com.schaefer.domain.model.CameraLensDomain
import org.junit.jupiter.api.Test

internal class CameraLensMapperKtTest {

    @Test
    fun `toCameraSelector should map DEFAULT_BACK_CAMERA to CameraSelector`() {
        val cameraLens = CameraLensDomain.DEFAULT_BACK_CAMERA

        val result = cameraLens.toCameraSelector()

        assert(result == CameraSelector.DEFAULT_BACK_CAMERA)
    }

    @Test
    fun `toCameraSelector should map DEFAULT_FRONT_CAMERA to CameraSelector`() {
        val cameraLens = CameraLensDomain.DEFAULT_FRONT_CAMERA

        val result = cameraLens.toCameraSelector()

        assert(result == CameraSelector.DEFAULT_FRONT_CAMERA)
    }
}
