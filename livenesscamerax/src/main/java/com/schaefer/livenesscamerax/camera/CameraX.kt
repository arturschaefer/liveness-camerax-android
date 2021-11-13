package com.schaefer.livenesscamerax.camera

import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleObserver
import com.schaefer.livenesscamerax.domain.model.FaceResult
import kotlinx.coroutines.flow.Flow

internal interface CameraX {
    fun startCamera(cameraPreviewView: PreviewView)
    fun enableFlash(enabled: Boolean)
    fun takePicture(takenByUser: Boolean)
    fun observeFaceList(): Flow<List<FaceResult>>
    fun observeLuminosity(): Flow<Double>
    fun getLifecycleObserver(): LifecycleObserver
    fun deleteAllPictures(): Boolean
    fun getAllPictures(): List<String>
}
