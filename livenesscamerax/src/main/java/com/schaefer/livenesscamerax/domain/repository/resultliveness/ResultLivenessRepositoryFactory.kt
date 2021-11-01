package com.schaefer.livenesscamerax.domain.repository.resultliveness

import com.schaefer.livenesscamerax.core.factory.Factory

internal object ResultLivenessRepositoryFactory : Factory<ResultLivenessRepository> {

    override fun create(): ResultLivenessRepository {
        return ResultLivenessRepositoryImpl()
    }
}
