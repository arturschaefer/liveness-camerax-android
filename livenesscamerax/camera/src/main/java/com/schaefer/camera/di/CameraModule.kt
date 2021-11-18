package com.schaefer.camera.di

import android.app.Application

object CameraModule {
    val container: CameraContainer by lazy { CameraContainer() }

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
