package com.schaefer.camera.core.processor.luminosity

import android.annotation.SuppressLint
import androidx.camera.core.ImageProxy
import com.schaefer.core.extensions.toByteArray
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

private const val PLAN_Y = 0
private const val WHITE = 0xFF

internal class LuminosityFrameProcessorImpl : LuminosityFrameProcessor {

    private val luminosityBroadcastChannel = MutableSharedFlow<Double>()

    override fun observeLuminosity(): Flow<Double> = luminosityBroadcastChannel.asSharedFlow()

    @SuppressLint("UnsafeOptInUsageError")
    override suspend fun onFrameCaptured(imageProxy: ImageProxy) {
        imageProxy.image?.let { image ->
            val buffer = image.planes[PLAN_Y].buffer
            val data = buffer.toByteArray()
            val pixels = data.map { it.toInt() and WHITE }

            luminosityBroadcastChannel.emit(pixels.average())

            imageProxy.close()
        }
    }
}
