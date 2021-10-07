package com.schaefer.livenesscamerax.presentation.provider

internal interface ResourceProvider {
    fun getString(resourceIdentifier: Int, vararg arguments: Any = arrayOf()): String

    fun getStringArray(resourceIdentifier: Int): Array<String>

    fun getInteger(resourceIdentifier: Int): Int

    fun getIntegerArray(resourceIdentifier: Int): Array<Int>

    fun getBoolean(resourceIdentifier: Int): Boolean

    fun getColor(resourceIdentifier: Int): Int
}