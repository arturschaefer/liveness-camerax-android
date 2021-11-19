package com.schaefer.livenesscamerax.navigation

import android.content.Context
import android.content.Intent
import com.schaefer.domain.model.LivenessCameraXResultDomain
import com.schaefer.livenesscamerax.domain.mapper.toPresentation
import com.schaefer.livenesscamerax.domain.model.CameraSettings
import com.schaefer.livenesscamerax.domain.model.LivenessCameraXResult
import com.schaefer.livenesscamerax.presentation.LivenessCameraXActivity

internal const val EXTRAS_LIVENESS_CAMERA_SETTINGS = "liveness_camerax_camera_settings"

object LivenessEntryPoint {

    var callbackResult: ((LivenessCameraXResult) -> Unit) = {}

    fun startLiveness(
        context: Context,
        cameraSettings: CameraSettings = CameraSettings(),
        callback: (LivenessCameraXResult) -> Unit
    ) {
        context.startActivity(
            Intent(context, LivenessCameraXActivity::class.java).apply {
                putExtra(EXTRAS_LIVENESS_CAMERA_SETTINGS, cameraSettings)
            }
        )
        callbackResult = callback
    }

    internal fun postResultCallback(livenessCameraXResult: LivenessCameraXResultDomain) {
        callbackResult.invoke(livenessCameraXResult.toPresentation())
    }
}
