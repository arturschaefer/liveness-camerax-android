package com.schaefer.livenesscamerax.domain.repository.checkliveness

import com.schaefer.core.factory.Factory
import com.schaefer.domain.repository.CheckLivenessRepository
import com.schaefer.livenesscamerax.domain.model.FaceResult

internal object CheckLivenessRepositoryFactory : Factory<CheckLivenessRepository<FaceResult>> {
    override fun create(): CheckLivenessRepository<FaceResult> {
        return CheckLivenessRepositoryImpl()
    }
}
