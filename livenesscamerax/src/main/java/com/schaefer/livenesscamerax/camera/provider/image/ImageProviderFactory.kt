package com.schaefer.livenesscamerax.camera.provider.image

import android.content.Context
import com.schaefer.livenesscamerax.core.factory.Factory

internal class ImageProviderFactory(private val context: Context) : Factory<ImageProvider> {

    override fun create(): ImageProvider {
        return ImageProviderImpl(context)
    }
}
