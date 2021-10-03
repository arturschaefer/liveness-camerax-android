package com.schaefer.livenessmlkit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.schaefer.livenesscamerax.presentation.LivenessCameraXActivity
import com.schaefer.livenessmlkit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvHelloWorld.setOnClickListener {
            startActivity(Intent(this, LivenessCameraXActivity::class.java))
        }
    }
}