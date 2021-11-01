package com.schaefer.livenesscamerax.domain.usecase.editphoto

import android.content.Context
import com.schaefer.livenesscamerax.core.factory.Factory
import com.schaefer.livenesscamerax.di.LibraryModule

internal object EditPhotoUseCaseFactory : Factory<EditPhotoUseCase> {

    private val context: Context by lazy {
        LibraryModule.container.provideContext()
    }

    override fun create(): EditPhotoUseCase {
        return EditPhotoUseCaseImpl(context)
    }
}
