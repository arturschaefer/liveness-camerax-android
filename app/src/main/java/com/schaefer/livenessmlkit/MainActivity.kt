package com.schaefer.livenessmlkit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.schaefer.livenesscamerax.domain.model.LivenessType
import com.schaefer.livenesscamerax.presentation.LivenessCameraXActivity
import com.schaefer.livenesscamerax.presentation.LivenessCameraXActivity.Companion.PHOTO_PATH_RESULT
import com.schaefer.livenesscamerax.presentation.LivenessCameraXActivity.Companion.REQUEST_CODE_LIVENESS
import com.schaefer.livenessmlkit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val startLiveness = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        Log.d(this.localClassName, result.resultCode.toString())

        if (result.resultCode == Activity.RESULT_OK) {
            // Handle the Intent
            val filePath = result.data?.getStringExtra(PHOTO_PATH_RESULT)

            Glide
                .with(this)
                .load(filePath)
                .centerCrop()
                .into(binding.ivResult)

            binding.ivResult.isVisible = true
        }
    }
    private val livenessList = arrayListOf(
        LivenessType.HAS_BLINKED,
        LivenessType.HEAD_LEFT,
        LivenessType.HEAD_RIGHT,
        LivenessType.HAS_SMILED,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        launchLivenessActivity()
    }

    private fun launchLivenessActivity() {
        val livenessIntent = Intent(this, LivenessCameraXActivity::class.java).apply {
            putParcelableArrayListExtra(REQUEST_CODE_LIVENESS, livenessList)
        }

        binding.btnStartLiveness.setOnClickListener {
            startLiveness.launch(livenessIntent)
        }
    }
}
