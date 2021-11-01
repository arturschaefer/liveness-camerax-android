package com.schaefer.livenesscamerax.domain.repository.file

import android.content.Context
import com.schaefer.livenesscamerax.core.factory.Factory
import com.schaefer.livenesscamerax.di.LibraryModule.container
import com.schaefer.livenesscamerax.domain.model.StorageType

internal object FileRepositoryFactory : Factory<FileRepository> {

    private val context: Context by lazy { container.provideContext() }

    var storageType: StorageType = StorageType.INTERNAL

    override fun create(): FileRepository {
        return CameraFileRepository(storageType, context)
    }
}
