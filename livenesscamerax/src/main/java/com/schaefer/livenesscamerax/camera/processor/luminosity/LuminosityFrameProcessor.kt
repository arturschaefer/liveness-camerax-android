package com.schaefer.livenesscamerax.camera.processor.luminosity

import com.schaefer.livenesscamerax.camera.processor.FrameProcessor
import kotlinx.coroutines.flow.Flow

internal interface LuminosityFrameProcessor : FrameProcessor {
    fun observeLuminosity(): Flow<Double>
}
