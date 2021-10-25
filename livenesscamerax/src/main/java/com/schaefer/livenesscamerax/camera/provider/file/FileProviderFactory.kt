package com.schaefer.livenesscamerax.camera.provider.file

import android.content.Context
import com.schaefer.livenesscamerax.core.factory.Factory
import com.schaefer.livenesscamerax.domain.model.StorageType

internal class FileProviderFactory(
    private val storageType: StorageType,
    private val context: Context
) : Factory<FileProvider> {

    override fun create(): FileProvider {
        return FileProviderImpl(storageType, context)
    }
}
