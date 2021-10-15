package com.schaefer.livenesscamerax.presentation.navigation

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import com.schaefer.livenesscamerax.presentation.model.CameraSettings
import com.schaefer.livenesscamerax.presentation.model.LivenessCameraXResult

interface LivenessCameraX {

    fun getIntent(
        cameraSettings: CameraSettings = CameraSettings(),
        context: Context
    ): Intent

    fun getDataResult(result: ActivityResult): LivenessCameraXResult?
}
