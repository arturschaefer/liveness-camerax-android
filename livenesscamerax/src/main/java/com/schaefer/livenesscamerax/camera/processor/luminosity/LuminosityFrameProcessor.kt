package com.schaefer.livenesscamerax.camera.processor.luminosity

import com.schaefer.livenesscamerax.camera.processor.FrameProcessor
import kotlinx.coroutines.flow.Flow

interface LuminosityFrameProcessor : FrameProcessor {
    fun getLuminosity(): Flow<Double>
}
