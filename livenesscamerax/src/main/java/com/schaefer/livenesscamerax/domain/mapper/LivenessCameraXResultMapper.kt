package com.schaefer.livenesscamerax.domain.mapper

import com.schaefer.domain.model.LivenessCameraXResultDomain
import com.schaefer.livenesscamerax.domain.model.LivenessCameraXResult

fun LivenessCameraXResultDomain.toPresentation(): LivenessCameraXResult {
    return LivenessCameraXResult(
        createdByUser = this.createdByUser?.toPresentation(),
        createdBySteps = this.createdBySteps?.map { it.toPresentation() },
        error = this.error?.toPresentation()
    )
}
