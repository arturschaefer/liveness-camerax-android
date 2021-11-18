package com.schaefer.livenesscamerax.domain.mapper

import com.schaefer.domain.model.PhotoResultDomain
import com.schaefer.livenesscamerax.presentation.model.PhotoResult

fun PhotoResultDomain.toPresentation(): PhotoResult {
    return PhotoResult(createdAt = this.createdAt, fileBase64 = this.fileBase64)
}
