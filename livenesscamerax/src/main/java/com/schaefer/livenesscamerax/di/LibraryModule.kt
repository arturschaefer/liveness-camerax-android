package com.schaefer.livenesscamerax.di

import android.app.Application

internal object LibraryModule {

    @Volatile
    lateinit var application: Application

    val container: LivenessCameraXContainer by lazy { LivenessCameraXContainer(application) }

    fun initializeDI(newApplication: Application) {
        if (!::application.isInitialized) {
            synchronized(this) {
                application = newApplication
            }
        }
    }
}
