package com.schaefer.livenesscamerax.camera.provider.file

import android.content.Context
import com.schaefer.livenesscamerax.core.factory.Factory
import com.schaefer.livenesscamerax.domain.model.StorageType

internal class FileHandlerFactory(
    private val storageType: StorageType,
    private val context: Context
) : Factory<FileHandler> {

    override fun create(): FileHandler {
        return FileHandlerImpl(storageType, context)
    }
}
