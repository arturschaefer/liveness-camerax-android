package com.schaefer.livenesscamerax.presentation.provider.result

import com.schaefer.livenesscamerax.core.factory.Factory

internal class ResultProviderFactory : Factory<ResultHandler> {

    override fun create(): ResultHandler {
        return ResultHandlerImpl()
    }
}
