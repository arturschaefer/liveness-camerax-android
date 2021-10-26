package com.schaefer.livenesscamerax.core.factory

interface Factory<T> {
    fun create(): T
}
