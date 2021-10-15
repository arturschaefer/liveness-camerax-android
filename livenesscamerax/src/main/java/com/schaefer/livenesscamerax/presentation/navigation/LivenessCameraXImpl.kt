package com.schaefer.livenesscamerax.presentation.navigation

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import com.schaefer.livenesscamerax.domain.model.StepLiveness
import com.schaefer.livenesscamerax.presentation.LivenessCameraXActivity
import com.schaefer.livenesscamerax.presentation.model.LivenessCameraXResult

internal const val REQUEST_CODE_LIVENESS = "liveness"
internal const val RESULT_LIVENESS_CAMERAX = "liveness_result_list_of_pictures"

internal class LivenessCameraXImpl : LivenessCameraX {

    override fun getIntent(livenessStepList: ArrayList<StepLiveness>, context: Context): Intent {
        return Intent(context, LivenessCameraXActivity::class.java).apply {
            putParcelableArrayListExtra(REQUEST_CODE_LIVENESS, livenessStepList)
        }
    }

    override fun getDataResult(result: ActivityResult): LivenessCameraXResult? {
        return result.data?.getParcelableExtra(RESULT_LIVENESS_CAMERAX)
    }
}
