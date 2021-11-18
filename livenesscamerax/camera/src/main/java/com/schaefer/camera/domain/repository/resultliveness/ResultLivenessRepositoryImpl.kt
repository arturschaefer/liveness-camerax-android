package com.schaefer.camera.domain.repository.resultliveness

import com.schaefer.camera.domain.mapper.toPhotoResult
import com.schaefer.domain.model.LivenessCameraXResultDomain
import com.schaefer.domain.model.PhotoResultDomain
import com.schaefer.domain.repository.ResultLivenessRepository

internal class ResultLivenessRepositoryImpl(
    private val resultCallback: (LivenessCameraXResultDomain) -> Unit
) : ResultLivenessRepository<PhotoResultDomain> {

    override fun success(photoResult: PhotoResultDomain, filesPath: List<String>) {
        val livenessCameraXResult =
            LivenessCameraXResultDomain(photoResult, filesPath.toPhotoResult())
        resultCallback(livenessCameraXResult)
    }

    override fun error(exception: Exception) {
        val livenessCameraXResult = LivenessCameraXResultDomain(exception)
        resultCallback(livenessCameraXResult)
    }
}
