package com.schaefer.livenesscamerax.presentation.fragment

import android.Manifest
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.schaefer.livenesscamerax.R
import com.schaefer.livenesscamerax.camera.CameraX
import com.schaefer.livenesscamerax.camera.CameraXImpl
import com.schaefer.livenesscamerax.camera.callback.CameraXCallback
import com.schaefer.livenesscamerax.camera.callback.CameraXCallbackImpl
import com.schaefer.livenesscamerax.core.extensions.shouldShowRequest
import com.schaefer.livenesscamerax.core.extensions.snack
import com.schaefer.livenesscamerax.databinding.LivenessCameraxFragmentBinding
import com.schaefer.livenesscamerax.domain.model.AnalyzeType
import com.schaefer.livenesscamerax.domain.model.CameraSettings
import com.schaefer.livenesscamerax.domain.model.LivenessType
import com.schaefer.livenesscamerax.presentation.LivenessCameraXActivity.Companion.PHOTO_PATH_RESULT
import com.schaefer.livenesscamerax.presentation.LivenessCameraXActivity.Companion.REQUEST_CODE_LIVENESS
import com.schaefer.livenesscamerax.presentation.provider.ResourceProviderImpl
import com.schaefer.livenesscamerax.presentation.viewmodel.LivenessAction
import com.schaefer.livenesscamerax.presentation.viewmodel.LivenessViewModel
import com.schaefer.livenesscamerax.presentation.viewmodel.LivenessViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File

@ExperimentalCoroutinesApi
@FlowPreview
@InternalCoroutinesApi
internal class CameraXFragment : Fragment(R.layout.liveness_camerax_fragment) {
    private var _binding: LivenessCameraxFragmentBinding? = null
    private val binding get() = _binding!!

    private val livenessViewModel: LivenessViewModel by viewModels {
        LivenessViewModelFactory(ResourceProviderImpl(requireContext()))
    }

    private val cameraXCallback: CameraXCallback by lazy {
        CameraXCallbackImpl(::handlePictureSuccess, ::handlePictureError, requireContext())
    }

    private val cameraX: CameraX by lazy {
        CameraXImpl(
            settings = CameraSettings(),
            cameraXCallback = cameraXCallback,
            analyzeType = AnalyzeType.FACE_PROCESSOR,
            lifecycleOwner = this,
            context = requireContext()
        )
    }

    private val cameraManifest = Manifest.permission.CAMERA
    private val cameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            with(binding.clRoot) {
                when {
                    granted -> permissionIsGranted()
                    requireActivity().shouldShowRequest(cameraManifest) -> {
                        snack(context.getString(R.string.liveness_camerax_message_permission_denied))
                    }
                    else -> snack(context.getString(R.string.liveness_camerax_message_permission_unknown))
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LivenessCameraxFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupLivenessSteps()
        cameraPermission.launch(cameraManifest)
    }

    private fun setupObservers() {
        livenessViewModel.state.observe(viewLifecycleOwner) { state ->
            Timber.d(state.messageLiveness)
            binding.tvStepText.text = state.messageLiveness
        }

        livenessViewModel.action.observe(viewLifecycleOwner) { action ->
            when (action) {
                LivenessAction.LivenessCompleted -> {
                    activity?.let {
                        it.setResult(RESULT_OK)
                        it.finish()
                    }
                }
                LivenessAction.LivenessCanceled -> {
                    activity?.setResult(RESULT_CANCELED)
                }
                is LivenessAction.EnableCameraButton -> {
                    binding.cameraCaptureButton.isVisible = true
                }
            }
        }
    }

    private fun setupLivenessSteps() {
        val validateRequested: List<LivenessType> =
            activity?.intent?.extras?.getParcelableArrayList(
                REQUEST_CODE_LIVENESS
            ) ?: arrayListOf()
        livenessViewModel.setupSteps(validateRequested)
    }

    private fun permissionIsGranted() {
        viewLifecycleOwner.lifecycleScope.launch {
            startCamera()
        }
    }

    private fun startCamera() {
        cameraX.startCamera(binding.viewFinder)

        livenessViewModel.let {
            it.observeFacesDetection(cameraX.getFacesFlowable())
            it.observeLuminosity(cameraX.getLuminosity())
        }

        lifecycle.addObserver(cameraX.getLifecycleObserver())

        binding.cameraCaptureButton.setOnClickListener {
            cameraX.takePicture()
        }

        binding.overlayView.apply {
            init()
            invalidate()
            isVisible = true
        }

        binding.tvStepText.isVisible = true
    }

    private fun handlePictureError(throwable: Throwable) {
        Timber.e(throwable)
        // TODO display some message and return error to activity
    }

    private fun handlePictureSuccess(file: File) {
        requireActivity().setResult(
            RESULT_OK,
            Intent().apply {
                putExtra(PHOTO_PATH_RESULT, file.path)
            }
        )
        requireActivity().finish()
    }
}
