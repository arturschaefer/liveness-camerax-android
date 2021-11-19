package com.schaefer.camera.di

import android.app.Application
import androidx.lifecycle.LifecycleOwner

object CameraModule {
    internal val container: CameraContainer by lazy { CameraContainer() }
    internal lateinit var lifecycleOwner: LifecycleOwner

    @Volatile
    lateinit var application: Application

    fun initializeDI(newApplication: Application) {
        if (!::application.isInitialized) {
            synchronized(this) {
                application = newApplication
            }
        }
    }
}
