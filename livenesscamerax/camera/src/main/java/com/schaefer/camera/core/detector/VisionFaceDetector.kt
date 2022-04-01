package com.schaefer.camera.core.detector

import android.annotation.SuppressLint
import androidx.camera.core.ImageProxy
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions

internal class VisionFaceDetector : FrameFaceDetector {

    private val realTimeOpts = FaceDetectorOptions.Builder()
        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .enableTracking()
        .build()

    private val detector: FaceDetector = FaceDetection.getClient(realTimeOpts)

    private fun processImage(
        image: InputImage,
        onSuccessListener: (List<Face>) -> Unit = {},
        onCompleteListener: () -> Unit = {}
    ): Task<List<Face>> {
        return detector.process(image)
            .addOnSuccessListener { faces -> if (faces.isNotEmpty()) onSuccessListener(faces) }
            .addOnCompleteListener {
                onCompleteListener.invoke()
            }
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun detect(imageProxy: ImageProxy, processImage: (List<Face>) -> Unit) {
        imageProxy.image?.let {
            val image: InputImage = InputImage.fromMediaImage(
                it,
                imageProxy.imageInfo.rotationDegrees
            )

            processImage(image, handleSuccessImage(processImage)) {
                imageProxy.close()
            }
        }
    }

    private fun handleSuccessImage(processImage: (List<Face>) -> Unit): (List<Face>) -> Unit = {
        processImage.invoke(it)
    }
}
