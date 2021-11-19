package com.schaefer.camera.domain.repository.file

import android.content.Context
import com.schaefer.camera.di.CameraModule.container
import com.schaefer.core.factory.Factory
import com.schaefer.domain.model.StorageTypeDomain
import com.schaefer.domain.repository.FileRepository

internal object FileRepositoryFactory : Factory<FileRepository> {

    private val context: Context by lazy { container.provideContext() }
    var storageType: StorageTypeDomain = StorageTypeDomain.INTERNAL

    override fun create(): FileRepository {
        return FileRepositoryImpl(storageType, context)
    }
}
