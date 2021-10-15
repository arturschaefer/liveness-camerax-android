package com.schaefer.livenesscamerax.presentation.navigation

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import com.schaefer.livenesscamerax.presentation.LivenessCameraXActivity
import com.schaefer.livenesscamerax.presentation.model.CameraSettings
import com.schaefer.livenesscamerax.presentation.model.LivenessCameraXResult

internal const val EXTRAS_LIVENESS_CAMERA_SETTINGS = "liveness_camerax_camera_settings"
internal const val RESULT_LIVENESS_CAMERAX = "liveness_result_list_of_pictures"

internal class LivenessCameraXImpl : LivenessCameraX {

    override fun getIntent(
        cameraSettings: CameraSettings,
        context: Context
    ): Intent {
        return Intent(context, LivenessCameraXActivity::class.java).apply {
            putExtra(EXTRAS_LIVENESS_CAMERA_SETTINGS, cameraSettings)
        }
    }

    override fun getDataResult(result: ActivityResult): LivenessCameraXResult? {
        return result.data?.getParcelableExtra(RESULT_LIVENESS_CAMERAX)
    }
}
