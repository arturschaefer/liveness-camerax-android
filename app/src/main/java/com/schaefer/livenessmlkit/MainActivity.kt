package com.schaefer.livenessmlkit

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.schaefer.livenesscamerax.domain.model.StepLiveness
import com.schaefer.livenesscamerax.domain.model.StorageType
import com.schaefer.livenesscamerax.presentation.model.CameraSettings
import com.schaefer.livenesscamerax.presentation.navigation.LivenessEntryPoint
import com.schaefer.livenessmlkit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val livenessEntryPoint = LivenessEntryPoint
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
                    livenessEntryPoint.startLiveness(
                        cameraSettings = CameraSettings(
                            livenessStepList = mutableStepList,
                            storageType = StorageType.INTERNAL
                        ),
                        context = this,
                    ) {
                        if (it.error == null) {
                            Glide
                                .with(this)
                                .load(it.createdByUser?.filePath)
                                .centerCrop()
                                .into(binding.ivResult)

                            binding.ivResult.isVisible = true
                        } else {
                            it.error?.let {
                                Log.e(
                                    this.localClassName,
                                    it.toString()
                                )
                            }
                        }
                    }
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
