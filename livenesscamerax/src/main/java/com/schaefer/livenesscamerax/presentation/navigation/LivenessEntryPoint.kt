package com.schaefer.livenesscamerax.presentation.navigation

import android.content.Context
import android.content.Intent
import com.schaefer.domain.model.LivenessCameraXResultDomain
import com.schaefer.livenesscamerax.domain.mapper.toPresentation
import com.schaefer.livenesscamerax.presentation.LivenessCameraXActivity
import com.schaefer.livenesscamerax.presentation.model.CameraSettings
import com.schaefer.livenesscamerax.presentation.model.LivenessCameraXResult

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
