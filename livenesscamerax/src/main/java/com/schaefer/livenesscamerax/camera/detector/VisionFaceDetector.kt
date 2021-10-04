package com.schaefer.livenesscamerax.camera.detector

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
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

    @SuppressLint("UnsafeExperimentalUsageError", "UnsafeOptInUsageError")
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun detect(imageProxy: ImageProxy): Channel<List<Face>> {
        val channel = Channel<List<Face>>()

        val image: InputImage = InputImage.fromMediaImage(
            imageProxy.image,
            imageProxy.imageInfo.rotationDegrees
        )

        processImage(image, channel) {
            imageProxy.close()
        }

        return channel
    }

    private fun processImage(
        image: InputImage,
        emitter: Channel<List<Face>>,
        onCompleteAction: () -> Unit = {},
    ): Task<List<Face>> {
        return detector.process(image)
            .addOnSuccessListener { faces ->
                CoroutineScope(IO).launch {
                    if (faces.isNotEmpty()) emitter.send(faces)
                }
            }
            .addOnCompleteListener {
                onCompleteAction.invoke()
            }
    }
}