package com.schaefer.camera.core.processor.face

import com.schaefer.camera.core.detector.VisionFaceDetector
import com.schaefer.camera.di.CameraModule.container
import com.schaefer.core.factory.Factory

internal object FaceFrameProcessorFactory : Factory<FaceFrameProcessor> {

    private val detector: VisionFaceDetector = container.provideVisionFaceDetector()

    override fun create(): FaceFrameProcessor {
        return FaceFrameProcessorImpl(container.provideCoroutineScope(), detector)
    }
}
