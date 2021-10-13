package com.schaefer.livenesscamerax.presentation.provider

import android.app.Activity
import android.content.Intent
import com.schaefer.livenesscamerax.presentation.LivenessCameraXActivity
import com.schaefer.livenesscamerax.presentation.model.LivenessCameraXResult

internal class SendResultImpl(
    private val activity: Activity
) : SendResult {

    override fun success(resultSuccess: LivenessCameraXResult) {
        activity.apply {
            setResult(
                Activity.RESULT_OK,
                Intent().apply {
                    putExtra(
                        LivenessCameraXActivity.RESULT_LIVENESS_CAMERAX,
                        resultSuccess
                    )
                }
            )
            finish()
        }
    }

    override fun error(exception: Exception) {
        activity.apply {
            setResult(
                Activity.RESULT_CANCELED,
                Intent().apply {
                    putExtra(
                        LivenessCameraXActivity.RESULT_LIVENESS_CAMERAX,
                        LivenessCameraXResult(exception)
                    )
                }
            )
        }
    }
}
