package com.schaefer.livenesscamerax.domain.mapper

import com.schaefer.core.mapper.Mapper
import com.schaefer.domain.model.AnalyzeTypeDomain
import com.schaefer.livenesscamerax.domain.model.AnalyzeType

class AnalyzeTypeToDomain : Mapper<AnalyzeType, AnalyzeTypeDomain> {

    override fun map(item: AnalyzeType): AnalyzeTypeDomain {
        return when (item) {
            AnalyzeType.FACE_PROCESSOR -> AnalyzeTypeDomain.FACE_PROCESSOR
            AnalyzeType.LUMINOSITY -> AnalyzeTypeDomain.LUMINOSITY
            AnalyzeType.COMPLETE -> AnalyzeTypeDomain.COMPLETE
        }
    }
}
