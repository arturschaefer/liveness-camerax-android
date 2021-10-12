package com.schaefer.livenesscamerax.camera.processor.luminosity

import android.annotation.SuppressLint
import androidx.camera.core.ImageProxy
import com.schaefer.livenesscamerax.core.extensions.toByteArray
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow

private const val PLAN_Y = 0
private const val WHITE = 0xFF

@ExperimentalCoroutinesApi
internal class LuminosityFrameProcessorImpl : LuminosityFrameProcessor {

    private val publishSubject = BroadcastChannel<Double>(Channel.BUFFERED)

    override fun getLuminosity(): Flow<Double> = publishSubject.openSubscription().consumeAsFlow()

    @SuppressLint("UnsafeExperimentalUsageError", "UnsafeOptInUsageError")
    override suspend fun onFrameCaptured(imageProxy: ImageProxy) {
        imageProxy.image?.let { image ->
            val buffer = image.planes[PLAN_Y].buffer
            val data = buffer.toByteArray()
            val pixels = data.map { it.toInt() and WHITE }

            publishSubject.send(pixels.average())

            imageProxy.close()
        }
    }
}
