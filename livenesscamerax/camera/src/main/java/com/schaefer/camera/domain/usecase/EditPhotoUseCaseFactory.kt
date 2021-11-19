package com.schaefer.camera.domain.usecase

import android.content.Context
import com.schaefer.camera.di.CameraModule.container
import com.schaefer.core.factory.Factory
import com.schaefer.domain.EditPhotoUseCase

internal object EditPhotoUseCaseFactory : Factory<EditPhotoUseCase> {

    private val context: Context by lazy { container.provideContext() }

    override fun create(): EditPhotoUseCase {
        return EditPhotoUseCaseImpl(context)
    }
}
