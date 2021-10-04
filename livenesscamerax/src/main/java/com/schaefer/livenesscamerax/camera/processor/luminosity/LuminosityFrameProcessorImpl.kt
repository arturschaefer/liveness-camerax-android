package com.schaefer.livenesscamerax.camera.processor.luminosity

import android.annotation.SuppressLint
import androidx.camera.core.ImageProxy
import com.schaefer.livenesscamerax.core.extensions.toByteArray
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import timber.log.Timber

private const val PLAN_Y = 0

@ObsoleteCoroutinesApi
@FlowPreview
@ExperimentalCoroutinesApi
class LuminosityFrameProcessorImpl : LuminosityFrameProcessor {

    private val publishSubject = BroadcastChannel<Double>(BUFFERED)

    override fun getLuminosity(): Flow<Double> = publishSubject.asFlow()

    @SuppressLint("UnsafeExperimentalUsageError", "UnsafeOptInUsageError")
    override fun onFrameCaptured(imageProxy: ImageProxy) {
        imageProxy.image?.let { image ->
            val buffer = image.planes[PLAN_Y].buffer
            val data = buffer.toByteArray()
            val pixels = data.map { it.toInt() and 0xFF }

            Timber.d("CONTINUA ANALISANDO")
            CoroutineScope(IO).launch { publishSubject.send(pixels.average()) }

            image.close()
        }

    }
}