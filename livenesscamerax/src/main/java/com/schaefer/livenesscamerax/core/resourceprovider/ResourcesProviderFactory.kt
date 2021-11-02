package com.schaefer.livenesscamerax.core.resourceprovider

import android.content.Context
import com.schaefer.livenesscamerax.core.factory.Factory
import com.schaefer.livenesscamerax.di.LibraryModule.container

internal object ResourcesProviderFactory : Factory<ResourcesProvider> {

    private val context: Context by lazy { container.provideContext() }

    override fun create(): ResourcesProvider {
        return ResourcesProviderImpl(context)
    }
}
