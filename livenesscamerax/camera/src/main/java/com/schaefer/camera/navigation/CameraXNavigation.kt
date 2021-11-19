package com.schaefer.camera.navigation

import androidx.lifecycle.LifecycleOwner
import com.schaefer.camera.CameraX
import com.schaefer.camera.core.callback.CameraXCallback
import com.schaefer.camera.di.CameraModule
import com.schaefer.camera.di.CameraModule.container
import com.schaefer.domain.model.CameraSettingsDomain

class CameraXNavigation(private val lifecycleOwner: LifecycleOwner) {

    init { initializeModuleLifecyle(lifecycleOwner) }

    fun provideCameraXModule(
        cameraSettings: CameraSettingsDomain,
        cameraXCallback: CameraXCallback,
    ): CameraX {
        return container.provideCameraX(cameraSettings, cameraXCallback, lifecycleOwner)
    }

    private fun initializeModuleLifecyle(lifecycleOwner: LifecycleOwner) {
        CameraModule.lifecycleOwner = lifecycleOwner
    }
}
