package com.schaefer.livenesscamerax.camera

import androidx.camera.view.PreviewView

interface CameraX {
    fun startCamera(cameraPreviewView: PreviewView)
    fun enableFlash(enabled: Boolean)
    fun takePicture()
}