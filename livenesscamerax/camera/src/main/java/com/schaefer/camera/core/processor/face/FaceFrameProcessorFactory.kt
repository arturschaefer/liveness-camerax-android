package com.schaefer.camera.core.processor.face

import androidx.lifecycle.lifecycleScope
import com.schaefer.camera.core.detector.VisionFaceDetector
import com.schaefer.camera.di.CameraModule
import com.schaefer.camera.di.CameraModule.container
import com.schaefer.core.factory.Factory
import kotlinx.coroutines.CoroutineScope

internal object FaceFrameProcessorFactory : Factory<FaceFrameProcessor> {

    private val coroutineScope: CoroutineScope = CameraModule.lifecycleOwner.lifecycleScope
    private val detector: VisionFaceDetector = container.provideVisionFaceDetector()

    override fun create(): FaceFrameProcessor {
        return FaceFrameProcessorImpl(coroutineScope, detector)
    }
}
