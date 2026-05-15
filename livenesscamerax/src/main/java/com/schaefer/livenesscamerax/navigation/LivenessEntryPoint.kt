package com.schaefer.livenesscamerax.navigation

import android.content.Context
import android.content.Intent
import com.schaefer.domain.model.LivenessCameraXResultDomain
import com.schaefer.livenesscamerax.domain.mapper.toPresentation
import com.schaefer.livenesscamerax.domain.model.CameraSettings
import com.schaefer.livenesscamerax.domain.model.LivenessCameraXResult
import com.schaefer.livenesscamerax.domain.model.StepLiveness
import com.schaefer.livenesscamerax.presentation.LivenessCameraXActivity

internal const val EXTRAS_LIVENESS_CAMERA_SETTINGS = "liveness_camerax_camera_settings"

object LivenessEntryPoint {

    var callbackResult: ((LivenessCameraXResult) -> Unit) = {}
    var onStepCompleted: ((StepLiveness) -> Unit)? = null

    fun startLiveness(
        context: Context,
        cameraSettings: CameraSettings = CameraSettings(),
        onStepCompleted: ((StepLiveness) -> Unit)? = null,
        callback: (LivenessCameraXResult) -> Unit
    ) {
        context.startActivity(
            Intent(context, LivenessCameraXActivity::class.java).apply {
                putExtra(EXTRAS_LIVENESS_CAMERA_SETTINGS, cameraSettings)
            }
        )
        callbackResult = callback
        LivenessEntryPoint.onStepCompleted = onStepCompleted
    }

    internal fun postResultCallback(livenessCameraXResult: LivenessCameraXResultDomain) {
        callbackResult.invoke(livenessCameraXResult.toPresentation())
    }

    internal fun postStepCompletedCallback(step: StepLiveness) {
        onStepCompleted?.invoke(step)
    }
}
