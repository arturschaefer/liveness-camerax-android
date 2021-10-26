package com.schaefer.livenesscamerax.presentation.provider.result

import android.content.Intent
import com.schaefer.livenesscamerax.presentation.model.LivenessCameraXResult
import com.schaefer.livenesscamerax.presentation.model.PhotoResult
import com.schaefer.livenesscamerax.presentation.navigation.RESULT_LIVENESS_CAMERAX

internal class ResultHandlerImpl : ResultHandler {

    override fun success(photoResult: PhotoResult, filesPath: List<String>): Intent {
        val livenessCameraXResult = LivenessCameraXResult(photoResult, filesPath)

        return Intent().apply {
            putExtra(RESULT_LIVENESS_CAMERAX, livenessCameraXResult)
        }
    }

    override fun error(exception: Exception): Intent {
        val livenessCameraXResult = LivenessCameraXResult(exception)

        return Intent().apply {
            putExtra(RESULT_LIVENESS_CAMERAX, livenessCameraXResult)
        }
    }
}
