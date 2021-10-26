package com.schaefer.livenesscamerax.di

import android.app.Application
import com.schaefer.livenesscamerax.camera.provider.file.FileProvider
import com.schaefer.livenesscamerax.camera.provider.file.FileProviderFactory
import com.schaefer.livenesscamerax.camera.provider.image.ImageProvider
import com.schaefer.livenesscamerax.camera.provider.image.ImageProviderFactory
import com.schaefer.livenesscamerax.domain.checker.LivenessChecker
import com.schaefer.livenesscamerax.domain.checker.LivenessCheckerFactory
import com.schaefer.livenesscamerax.domain.model.StorageType
import com.schaefer.livenesscamerax.presentation.provider.resource.ResourcesProvider
import com.schaefer.livenesscamerax.presentation.provider.resource.ResourcesProviderFactory
import com.schaefer.livenesscamerax.presentation.provider.result.ResultProvider
import com.schaefer.livenesscamerax.presentation.provider.result.ResultProviderFactory

internal class LivenessCameraXContainer(private val application: Application) {

    val resourceProvider: ResourcesProvider by lazy {
        ResourcesProviderFactory(application.applicationContext).create()
    }

    val resultProvider: ResultProvider by lazy {
        ResultProviderFactory().create()
    }

    val livenessChecker: LivenessChecker by lazy {
        LivenessCheckerFactory().create()
    }

    val imageProvider: ImageProvider by lazy {
        ImageProviderFactory(application.applicationContext).create()
    }

    // TODO improve this
    fun fileProvider(storageType: StorageType): FileProvider =
        FileProviderFactory(storageType, application.applicationContext).create()
}
