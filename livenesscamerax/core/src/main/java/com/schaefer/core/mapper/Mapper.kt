package com.schaefer.core.mapper

interface Mapper<IN, OUT> {
    fun map(item: IN): OUT
}
