package com.schaefer.livenesscamerax.domain.model

import com.schaefer.domain.model.StorageTypeDomain

enum class StorageType {
    INTERNAL,
    EXTERNAL
}

fun StorageType.toDomain(): StorageTypeDomain {
    return when (this) {
        StorageType.INTERNAL -> StorageTypeDomain.INTERNAL
        StorageType.EXTERNAL -> StorageTypeDomain.EXTERNAL
    }
}
