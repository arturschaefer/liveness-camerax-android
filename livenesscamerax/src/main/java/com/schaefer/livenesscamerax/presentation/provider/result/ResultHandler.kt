package com.schaefer.livenesscamerax.presentation.provider.result

import android.content.Intent
import com.schaefer.livenesscamerax.presentation.model.PhotoResult

internal interface ResultHandler {
    fun success(photoResult: PhotoResult, filesPath: List<String>): Intent
    fun error(exception: Exception): Intent
}
