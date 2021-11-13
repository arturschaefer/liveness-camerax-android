package com.schaefer.livenesscamerax.domain.repository.file

import android.content.Context
import com.schaefer.domain.repository.FileRepository
import com.schaefer.livenesscamerax.di.LibraryModule.container
import com.schaefer.livenesscamerax.domain.model.StorageType

internal object FileRepositoryFactory : com.schaefer.core.factory.Factory<FileRepository> {

    private val context: Context by lazy { container.provideContext() }

    var storageType: StorageType = StorageType.INTERNAL

    override fun create(): FileRepository {
        return FileRepositoryImpl(storageType, context)
    }
}
