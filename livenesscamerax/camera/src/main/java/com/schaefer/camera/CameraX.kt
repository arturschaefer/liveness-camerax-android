package com.schaefer.camera

import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleObserver
import com.schaefer.camera.domain.model.FaceResult
import kotlinx.coroutines.flow.Flow

interface CameraX {
    fun startCamera(cameraPreviewView: PreviewView)
    fun enableFlash(enabled: Boolean)
    fun takePicture(takenByUser: Boolean = false)
    fun observeFaceList(): Flow<List<FaceResult>>
    fun observeLuminosity(): Flow<Double>
    fun getLifecycleObserver(): LifecycleObserver
    fun deleteAllPictures(): Boolean
    fun getAllPictures(): List<String>
}
