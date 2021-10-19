package com.schaefer.livenessmlkit

import android.app.Activity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.schaefer.livenesscamerax.domain.model.StepLiveness
import com.schaefer.livenesscamerax.presentation.LivenessCameraXActivity
import com.schaefer.livenesscamerax.presentation.model.CameraSettings
import com.schaefer.livenessmlkit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val startLiveness = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->

        if (result.resultCode == Activity.RESULT_OK) {
            val livenessCameraXResult = LivenessCameraXActivity.getLivenessDataResult(result)

            Glide
                .with(this)
                .load(livenessCameraXResult?.createdByUser?.filePath)
                .centerCrop()
                .into(binding.ivResult)

            binding.ivResult.isVisible = true
        }
    }
    private val livenessStepList = arrayListOf(
        StepLiveness.STEP_SMILE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        launchLivenessActivity()
    }

    private fun launchLivenessActivity() {
        binding.btnStartLiveness.setOnClickListener {
            startLiveness.launch(
                LivenessCameraXActivity.getLivenessIntent(
                    cameraSettings = CameraSettings(livenessStepList = livenessStepList),
                    context = this
                )
            )
        }
    }
}
