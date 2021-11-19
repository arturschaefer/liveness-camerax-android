package com.schaefer.camera.core.processor.luminosity

import com.schaefer.camera.core.processor.FrameProcessor
import kotlinx.coroutines.flow.Flow

internal interface LuminosityFrameProcessor : FrameProcessor {
    fun observeLuminosity(): Flow<Double>
}
