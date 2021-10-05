package com.schaefer.livenesscamerax.camera.processor.luminosity

import android.annotation.SuppressLint
import androidx.camera.core.ImageProxy
import com.schaefer.livenesscamerax.core.extensions.toByteArray
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

private const val PLAN_Y = 0

@FlowPreview
@ExperimentalCoroutinesApi
class LuminosityFrameProcessorImpl : LuminosityFrameProcessor {

    private val publishSubject = BroadcastChannel<Double>(BUFFERED)

    override fun getLuminosity(): Flow<Double> = publishSubject.asFlow()

    @SuppressLint("UnsafeOptInUsageError")
    override suspend fun onFrameCaptured(imageProxy: ImageProxy) {
        imageProxy.image?.let { image ->
            val buffer = image.planes[PLAN_Y].buffer
            val data = buffer.toByteArray()
            val pixels = data.map { it.toInt() and 0xFF }

            publishSubject.send(pixels.average())

            image.close()
        }

    }
}