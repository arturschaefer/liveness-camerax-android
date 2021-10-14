package com.schaefer.livenesscamerax.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.schaefer.livenesscamerax.databinding.LivenessCameraxActivityBinding

class LivenessCameraXActivity : AppCompatActivity() {

    private lateinit var binding: LivenessCameraxActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LivenessCameraxActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    companion object {
        const val EXTRAS_LIVENESS_STEPS = "liveness_camerax_steps"
        const val EXTRAS_LIVENESS_CAMERA_SETTINGS = "liveness_camerax_camera_settings"
        const val RESULT_LIVENESS_CAMERAX = "liveness_result_list_of_pictures"
    }
}
