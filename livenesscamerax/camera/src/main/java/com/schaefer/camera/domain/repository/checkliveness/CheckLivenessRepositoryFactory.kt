package com.schaefer.camera.domain.repository.checkliveness

import com.schaefer.camera.domain.model.FaceResult
import com.schaefer.core.factory.Factory
import com.schaefer.domain.repository.LivenessRepository

object CheckLivenessRepositoryFactory : Factory<LivenessRepository<FaceResult>> {
    override fun create(): LivenessRepository<FaceResult> {
        return LivenessRepositoryImpl()
    }
}
