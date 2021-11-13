package com.schaefer.core.extensions

import android.media.Image

private const val PLANE_Y = 0
private const val INTEGER_255 = 0xFF
private const val LUMINOSITY_ZERO = 0F

fun Image?.getLuminosity(): Float {
    return this?.let { image ->
        val buffer = image.planes[PLANE_Y].buffer
        val data = buffer.toByteArray()
        val pixels = data.map { byteArray ->
            byteArray.toInt() and INTEGER_255
        }
        pixels.average().toFloat()
    } ?: LUMINOSITY_ZERO
}
