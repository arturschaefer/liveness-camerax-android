package com.schaefer.livenesscamerax.domain.repository.resultliveness

import com.schaefer.core.factory.Factory
import com.schaefer.domain.repository.ResultLivenessRepository
import com.schaefer.livenesscamerax.presentation.model.PhotoResult

internal object ResultLivenessRepositoryFactory :
    Factory<ResultLivenessRepository<PhotoResult>> {

    override fun create(): ResultLivenessRepository<PhotoResult> {
        return ResultLivenessRepositoryImpl()
    }
}
