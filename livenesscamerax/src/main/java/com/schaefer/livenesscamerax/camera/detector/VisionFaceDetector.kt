package com.schaefer.livenesscamerax.camera.detector

import android.annotation.SuppressLint
import androidx.camera.core.ImageProxy
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
internal class VisionFaceDetector : FrameFaceDetector {

    private val realTimeOpts = FaceDetectorOptions.Builder()
        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .enableTracking()
        .build()

    private val detector: FaceDetector = FaceDetection.getClient(realTimeOpts)

    @SuppressLint("UnsafeOptInUsageError")
    override suspend fun detect(imageProxy: ImageProxy): BroadcastChannel<List<Face>> {
        val channel = BroadcastChannel<List<Face>>(1)

        val image: InputImage = InputImage.fromMediaImage(
            imageProxy.image,
            imageProxy.imageInfo.rotationDegrees
        )

        processImage(image, channel)

        return channel
    }

    private suspend fun processImage(
        image: InputImage,
        emitter: BroadcastChannel<List<Face>>
    ): Task<List<Face>> {
        return detector.process(image)
            .addOnSuccessListener { faces ->
                CoroutineScope(IO).launch {
                    if (faces.isNotEmpty()) emitter.send(faces)
                }
            }
    }
}