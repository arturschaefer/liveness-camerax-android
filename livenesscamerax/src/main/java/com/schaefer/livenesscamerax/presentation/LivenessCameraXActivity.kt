package com.schaefer.livenesscamerax.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.schaefer.livenesscamerax.databinding.ActivityLivenesscameraxBinding

class LivenessCameraXActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLivenesscameraxBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLivenesscameraxBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    companion object{
        const val REQUEST_CODE_LIVENESS = "liveness"
    }
}