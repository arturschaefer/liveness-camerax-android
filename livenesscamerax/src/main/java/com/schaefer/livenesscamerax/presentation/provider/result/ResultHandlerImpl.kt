package com.schaefer.livenesscamerax.presentation.provider.result

import com.schaefer.livenesscamerax.di.LibraryModule.container
import com.schaefer.livenesscamerax.presentation.model.LivenessCameraXResult
import com.schaefer.livenesscamerax.presentation.model.PhotoResult

internal class ResultHandlerImpl : ResultHandler {

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
