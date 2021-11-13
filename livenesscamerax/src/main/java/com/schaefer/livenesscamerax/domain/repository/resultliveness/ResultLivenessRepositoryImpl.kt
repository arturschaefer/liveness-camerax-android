package com.schaefer.livenesscamerax.domain.repository.resultliveness

import com.schaefer.domain.repository.ResultLivenessRepository
import com.schaefer.livenesscamerax.di.LibraryModule.container
import com.schaefer.livenesscamerax.presentation.model.LivenessCameraXResult
import com.schaefer.livenesscamerax.presentation.model.PhotoResult

internal class ResultLivenessRepositoryImpl : ResultLivenessRepository<PhotoResult> {

    private val resultCallback = container.provideLivenessEntryPoint

    override fun success(photoResult: PhotoResult, filesPath: List<String>) {
        val livenessCameraXResult = LivenessCameraXResult(photoResult, filesPath)
        resultCallback.postResultCallback(livenessCameraXResult)
    }

    override fun error(exception: Exception) {
        val livenessCameraXResult = LivenessCameraXResult(exception)
        resultCallback.postResultCallback(livenessCameraXResult)
    }
}
