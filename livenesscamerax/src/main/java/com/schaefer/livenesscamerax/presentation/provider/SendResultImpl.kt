package com.schaefer.livenesscamerax.presentation.provider

import android.app.Activity
import android.content.Intent
import com.schaefer.livenesscamerax.presentation.model.LivenessCameraXResult
import com.schaefer.livenesscamerax.presentation.model.PhotoResult
import com.schaefer.livenesscamerax.presentation.navigation.RESULT_LIVENESS_CAMERAX

internal class SendResultImpl(
    private val activity: Activity
) : SendResult {

    override fun success(photoResult: PhotoResult, filesPath: List<String>) {
        val livenessCameraXResult = LivenessCameraXResult(photoResult, filesPath)

        activity.apply {
            setResult(
                Activity.RESULT_OK,
                Intent().apply {
                    putExtra(RESULT_LIVENESS_CAMERAX, livenessCameraXResult)
                }
            )
            finish()
        }
    }

    override fun error(exception: Exception) {
        val livenessCameraXResult = LivenessCameraXResult(exception)

        activity.apply {
            setResult(
                Activity.RESULT_CANCELED,
                Intent().apply {
                    putExtra(RESULT_LIVENESS_CAMERAX, livenessCameraXResult)
                }
            )
        }
    }
}
