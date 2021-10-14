package com.schaefer.livenessmlkit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.schaefer.livenesscamerax.domain.model.StepLiveness
import com.schaefer.livenesscamerax.presentation.LivenessCameraXActivity
import com.schaefer.livenesscamerax.presentation.LivenessCameraXActivity.Companion.RESULT_LIVENESS_CAMERAX
import com.schaefer.livenesscamerax.presentation.model.CameraSettings
import com.schaefer.livenesscamerax.presentation.model.LivenessCameraXResult
import com.schaefer.livenessmlkit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val startLiveness = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->

        if (result.resultCode == Activity.RESULT_OK) {
            val livenessCameraXResult =
                result.data?.getParcelableExtra<LivenessCameraXResult>(RESULT_LIVENESS_CAMERAX)

            Glide
                .with(this)
                .load(livenessCameraXResult?.createdByUser?.filePath)
                .centerCrop()
                .into(binding.ivResult)

            binding.ivResult.isVisible = true
        }
    }

    private val livenessList = arrayListOf(
        StepLiveness.STEP_HEAD_LEFT,
        StepLiveness.STEP_BLINK
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        launchLivenessActivity()
    }

    private fun launchLivenessActivity() {
        val livenessIntent = Intent(this, LivenessCameraXActivity::class.java).apply {
            putParcelableArrayListExtra(LivenessCameraXActivity.EXTRAS_LIVENESS_STEPS, livenessList)
            putExtra(LivenessCameraXActivity.EXTRAS_LIVENESS_CAMERA_SETTINGS, CameraSettings())
        }

        binding.btnStartLiveness.setOnClickListener {
            startLiveness.launch(livenessIntent)
        }
    }
}
