package com.schaefer.livenesscamerax.presentation.provider.resource

import android.content.Context
import com.schaefer.livenesscamerax.core.factory.Factory

internal class ResourcesProviderFactory(
    private val context: Context
) : Factory<ResourcesProvider> {

    override fun create(): ResourcesProvider {
        return ResourcesProviderImpl(context)
    }
}
