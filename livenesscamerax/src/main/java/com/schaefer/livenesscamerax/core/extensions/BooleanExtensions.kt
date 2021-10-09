package com.schaefer.livenesscamerax.core.extensions

internal fun Boolean?.orFalse() = this ?: false

internal fun Boolean?.orTrue() = this ?: true
