package com.schaefer.core.factory

interface Factory<T> {
    fun create(): T
}
