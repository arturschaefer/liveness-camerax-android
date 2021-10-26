package com.schaefer.livenesscamerax.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.AppCompatActivity
import com.schaefer.livenesscamerax.databinding.LivenessCameraxActivityBinding
import com.schaefer.livenesscamerax.di.LibraryModule
import com.schaefer.livenesscamerax.presentation.model.CameraSettings
import com.schaefer.livenesscamerax.presentation.navigation.LivenessCameraX
import com.schaefer.livenesscamerax.presentation.navigation.LivenessCameraXImpl

private val livenessCameraX: LivenessCameraX by lazy {
    LivenessCameraXImpl()
}

class LivenessCameraXActivity : AppCompatActivity() {

    private lateinit var binding: LivenessCameraxActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LivenessCameraxActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        LibraryModule.initializeDI(application)
    }

    companion object {
        fun getLivenessIntent(
            cameraSettings: CameraSettings = CameraSettings(),
            context: Context
        ) = livenessCameraX.getIntent(cameraSettings, context)

        fun getLivenessDataResult(result: ActivityResult) =
            livenessCameraX.getDataResult(result)
    }
}
