package com.schaefer.livenessmlkit

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.schaefer.livenesscamerax.domain.model.StepLiveness
import com.schaefer.livenesscamerax.presentation.LivenessCameraXActivity
import com.schaefer.livenesscamerax.presentation.model.CameraSettings
import com.schaefer.livenessmlkit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val startLiveness = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->

        when (result.resultCode) {
            Activity.RESULT_OK -> {
                val livenessCameraXResult = LivenessCameraXActivity.getLivenessDataResult(result)

                Glide
                    .with(this)
                    .load(livenessCameraXResult?.createdByUser?.filePath)
                    .centerCrop()
                    .into(binding.ivResult)

                binding.ivResult.isVisible = true
            }
            else -> {
                Log.e(
                    this.localClassName,
                    LivenessCameraXActivity.getLivenessDataResult(result).toString()
                )
            }
        }
    }

    private val mutableStepList = arrayListOf<StepLiveness>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        launchLivenessActivity()
    }

    private fun launchLivenessActivity() {
        binding.btnStartLiveness.setOnClickListener {
            getSelectedSteps()
            when (mutableStepList.isEmpty()) {
                true -> showToast()
                false ->
                    startLiveness.launch(
                        LivenessCameraXActivity.getLivenessIntent(
                            cameraSettings = CameraSettings(livenessStepList = mutableStepList),
                            context = this
                        )
                    )
            }
        }
    }

    private fun getSelectedSteps() {
        mutableStepList.clear()
        binding.chipGroupSteps.forEach { chip ->
            (chip as Chip).takeIf { it.isChecked }?.let {
                mutableStepList.add(it.mapToLivenessStep())
            }
        }
    }

    private fun View.mapToLivenessStep(): StepLiveness {
        return when (this.id) {
            R.id.chipStepSmile -> StepLiveness.STEP_SMILE
            R.id.chipStepBlink -> StepLiveness.STEP_BLINK
            else -> StepLiveness.STEP_LUMINOSITY
        }
    }

    private fun showToast() {
        Toast.makeText(
            this,
            "You need to select at least one step",
            Toast.LENGTH_LONG
        ).show()
    }
}
