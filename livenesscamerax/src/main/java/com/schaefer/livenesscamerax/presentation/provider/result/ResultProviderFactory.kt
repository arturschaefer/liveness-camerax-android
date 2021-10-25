package com.schaefer.livenesscamerax.presentation.provider.result

import com.schaefer.livenesscamerax.core.factory.Factory

internal class ResultProviderFactory : Factory<ResultProvider> {

    override fun create(): ResultProvider {
        return ResultProviderImpl()
    }
}
