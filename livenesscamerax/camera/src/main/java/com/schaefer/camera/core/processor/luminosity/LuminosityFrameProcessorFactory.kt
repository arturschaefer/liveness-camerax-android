package com.schaefer.camera.core.processor.luminosity

import com.schaefer.core.factory.Factory

object LuminosityFrameProcessorFactory : Factory<LuminosityFrameProcessor> {
    override fun create(): LuminosityFrameProcessor {
        return LuminosityFrameProcessorImpl()
    }
}
