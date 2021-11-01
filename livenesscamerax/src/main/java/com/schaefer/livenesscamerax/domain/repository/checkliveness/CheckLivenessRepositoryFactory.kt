package com.schaefer.livenesscamerax.domain.repository.checkliveness

import com.schaefer.livenesscamerax.core.factory.Factory

internal object CheckLivenessRepositoryFactory : Factory<CheckLivenessRepository> {
    override fun create(): CheckLivenessRepository {
        return CheckLivenessRepositoryImpl()
    }
}
