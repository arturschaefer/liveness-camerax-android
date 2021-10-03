package com.schaefer.livenesscamerax.core.mapper

interface Mapper<IN, OUT> {
    fun map(item: IN): OUT
}