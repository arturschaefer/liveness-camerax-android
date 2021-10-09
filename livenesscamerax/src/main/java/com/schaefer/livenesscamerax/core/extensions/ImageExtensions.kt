package com.schaefer.livenesscamerax.core.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.media.Image
import android.os.Build
import androidx.annotation.RequiresApi
import java.io.ByteArrayOutputStream

private const val PLANE_Y = 0
private const val PLANE_VU = 2
private const val INTEGER_255 = 0xFF
private const val LUMINOSITY_ZERO = 0F

@RequiresApi(Build.VERSION_CODES.KITKAT)
internal fun Image.toBitmap(): Bitmap {
    val yBuffer = this.planes[PLANE_Y].buffer // Y
    val vuBuffer = this.planes[PLANE_VU].buffer // VU

    val ySize = yBuffer.remaining()
    val vuSize = vuBuffer.remaining()

    val nv21 = ByteArray(ySize + vuSize)

    yBuffer.get(nv21, 0, ySize)
    vuBuffer.get(nv21, ySize, vuSize)

    val yuvImage = YuvImage(nv21, ImageFormat.NV21, this.width, this.height, null)
    val out = ByteArrayOutputStream()
    yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 50, out)
    val imageBytes = out.toByteArray()
    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}

internal fun Image?.getLuminosity(): Float {
    return this?.let { image ->
        val buffer = image.planes[PLANE_Y].buffer
        val data = buffer.toByteArray()
        val pixels = data.map { byteArray ->
            byteArray.toInt() and INTEGER_255
        }
        pixels.average().toFloat()
    } ?: LUMINOSITY_ZERO
}
