package com.schaefer.camera.core.callback

import com.schaefer.core.factory.Factory
import com.schaefer.domain.model.PhotoResultDomain

object CameraXCallbackFactory : Factory<CameraXCallback> {

    var onImageSavedAction: (PhotoResultDomain, Boolean) -> Unit = { _, _ -> }
    var onErrorAction: (Exception) -> Unit = {}

    override fun create(): CameraXCallback {
        return CameraXCallbackImpl(onImageSavedAction, onErrorAction)
    }
}
