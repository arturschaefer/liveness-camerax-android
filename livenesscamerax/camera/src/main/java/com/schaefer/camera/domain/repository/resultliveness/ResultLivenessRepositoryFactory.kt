package com.schaefer.camera.domain.repository.resultliveness

import com.schaefer.core.factory.Factory
import com.schaefer.domain.model.LivenessCameraXResultDomain
import com.schaefer.domain.model.PhotoResultDomain
import com.schaefer.domain.repository.ResultLivenessRepository

object ResultLivenessRepositoryFactory : Factory<ResultLivenessRepository<PhotoResultDomain>> {

    var resultCallback: (LivenessCameraXResultDomain) -> Unit = { }

    override fun create(): ResultLivenessRepository<PhotoResultDomain> {
        return ResultLivenessRepositoryImpl(resultCallback)
    }
}
