package com.schaefer.camera.core.processor.luminosity

import com.schaefer.camera.core.processor.FrameProcessor
import kotlinx.coroutines.flow.Flow

interface LuminosityFrameProcessor : FrameProcessor {
    fun observeLuminosity(): Flow<Double>
}
