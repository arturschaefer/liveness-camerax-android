package com.schaefer.livenesscamerax.domain.repository.checkliveness

import com.schaefer.camera.domain.model.FaceResult
import com.schaefer.core.factory.Factory
import com.schaefer.domain.repository.CheckLivenessRepository

internal object CheckLivenessRepositoryFactory : Factory<CheckLivenessRepository<FaceResult>> {
    override fun create(): CheckLivenessRepository<FaceResult> {
        return CheckLivenessRepositoryImpl()
    }
}
