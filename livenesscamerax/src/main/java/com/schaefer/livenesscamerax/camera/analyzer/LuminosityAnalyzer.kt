package com.schaefer.livenesscamerax.camera.analyzer

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import java.nio.ByteBuffer

// Reference: https://developer.android.com/codelabs/camerax-getting-started#5
internal class LuminosityAnalyzer(
    private val listener: (Double) -> Unit
) : ImageAnalysis.Analyzer {

    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind() // Rewind the buffer to zero
        val data = ByteArray(remaining())
        get(data) // Copy the buffer into a byte array
        return data // Return the byte array
    }

    override fun analyze(image: ImageProxy) {

        val buffer = image.planes[0].buffer
        val data = buffer.toByteArray()
        val pixels = data.map { it.toInt() and 0xFF }

        listener(pixels.average())

        image.close()
    }
}
