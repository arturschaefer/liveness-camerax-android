package com.schaefer.camera.processor.luminosity

import com.schaefer.camera.processor.FrameProcessor
import kotlinx.coroutines.flow.Flow

interface LuminosityFrameProcessor : FrameProcessor {
    fun observeLuminosity(): Flow<Double>
}
