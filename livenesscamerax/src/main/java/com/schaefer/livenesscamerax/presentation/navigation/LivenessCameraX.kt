package com.schaefer.livenesscamerax.presentation.navigation

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import com.schaefer.livenesscamerax.domain.model.StepLiveness
import com.schaefer.livenesscamerax.presentation.model.LivenessCameraXResult

interface LivenessCameraX {

    fun getIntent(livenessStepList: ArrayList<StepLiveness>, context: Context): Intent

    fun getDataResult(result: ActivityResult): LivenessCameraXResult?
}
