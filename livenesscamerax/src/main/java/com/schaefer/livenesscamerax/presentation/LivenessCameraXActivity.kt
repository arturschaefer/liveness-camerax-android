package com.schaefer.livenesscamerax.presentation

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.schaefer.livenesscamerax.R
import com.schaefer.livenesscamerax.databinding.ActivityLivenesscameraxBinding

class LivenessCameraXActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLivenesscameraxBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLivenesscameraxBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}