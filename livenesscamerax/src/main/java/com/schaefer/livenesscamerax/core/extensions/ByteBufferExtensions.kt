package com.schaefer.livenesscamerax.core.extensions

import java.nio.ByteBuffer

internal fun ByteBuffer.toByteArray(): ByteArray {
    rewind() // Rewind the buffer to zero
    val data = ByteArray(remaining())
    get(data) // Copy the buffer into a byte array
    return data // Return the byte array
}
