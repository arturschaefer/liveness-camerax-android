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
import com.schaefer.livenesscamerax.camera.provider.FileProviderImpl
import com.schaefer.livenesscamerax.core.extensions.orFalse
import com.schaefer.livenesscamerax.core.extensions.shouldShowRequest
import com.schaefer.livenesscamerax.core.extensions.snack
import com.schaefer.livenesscamerax.databinding.LivenessCameraxFragmentBinding
import com.schaefer.livenesscamerax.domain.logic.LivenessCheckerImpl
import com.schaefer.livenesscamerax.domain.model.CameraSettings
import com.schaefer.livenesscamerax.domain.model.StepLiveness
import com.schaefer.livenesscamerax.presentation.LivenessCameraXActivity.Companion.REQUEST_CODE_LIVENESS
import com.schaefer.livenesscamerax.presentation.LivenessCameraXActivity.Companion.RESULT_LIVENESS_CAMERAX
import com.schaefer.livenesscamerax.presentation.model.LivenessCameraXError
import com.schaefer.livenesscamerax.presentation.model.LivenessCameraXResult
import com.schaefer.livenesscamerax.presentation.model.PhotoResult
import com.schaefer.livenesscamerax.presentation.provider.ResourcesProviderImpl
import com.schaefer.livenesscamerax.presentation.viewmodel.LivenessAction
import com.schaefer.livenesscamerax.presentation.viewmodel.LivenessViewModel
import com.schaefer.livenesscamerax.presentation.viewmodel.LivenessViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@FlowPreview
@InternalCoroutinesApi
internal class CameraXFragment : Fragment(R.layout.liveness_camerax_fragment) {
    private var _binding: LivenessCameraxFragmentBinding? = null
    private val binding get() = _binding!!

    private val livenessViewModel: LivenessViewModel by viewModels {
        LivenessViewModelFactory(ResourcesProviderImpl(requireContext()), LivenessCheckerImpl())
    }

    private val cameraXCallback: CameraXCallback by lazy {
        CameraXCallbackImpl(
            ::handlePictureSuccess,
            ::handlePictureError
        )
    }

    // TODO get CameraSettings from bundle
    // TODO create a fileProvider factory
    private val cameraSettings = CameraSettings()

    private val cameraX: CameraX by lazy {
        CameraXImpl(
            settings = cameraSettings,
            cameraXCallback = cameraXCallback,
            lifecycleOwner = this,
            context = requireContext(),
            fileProvider = FileProviderImpl(cameraSettings, requireContext())
        )
    }

    private val cameraManifest = Manifest.permission.CAMERA
    private val cameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            handleCameraPermission(granted.orFalse(), binding.clRoot)
        }

    private fun handleCameraPermission(granted: Boolean, parentView: View) {
        when {
            granted -> permissionIsGranted()
            requireActivity().shouldShowRequest(cameraManifest) -> {
                parentView.snack(
                    requireContext().getString(R.string.liveness_camerax_message_permission_denied)
                )
            }
            else -> parentView.snack(
                requireContext().getString(R.string.liveness_camerax_message_permission_unknown)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
            binding.tvStepText.text = state.messageLiveness
            binding.cameraCaptureButton.isVisible = state.isButtonEnabled
        }

        livenessViewModel.action.observe(viewLifecycleOwner) { action ->
            when (action) {
                LivenessAction.LivenessCanceled -> {
                    // TODO call this case on canceled or error
                    activity?.setResult(RESULT_CANCELED)
                }
                is LivenessAction.LivenessCompleted -> {
                    // TODO
                }
            }
        }
    }

    private fun setupLivenessSteps() {
        val validateRequested: List<StepLiveness> =
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

    // TODO simulate this scenario
    private fun handlePictureError(throwable: Exception) {
        requireActivity().setResult(RESULT_CANCELED,
            Intent().apply
            {
                putExtra(
                    RESULT_LIVENESS_CAMERAX,
                    LivenessCameraXResult(
                        error = LivenessCameraXError(
                            message = throwable.message.orEmpty(),
                            cause = throwable.cause.toString(),
                            exception = throwable
                        )
                    )
                )
            })
        requireActivity().finish()
    }

    private fun handlePictureSuccess(photoResult: PhotoResult) {
        val livenessPhotoResult = LivenessCameraXResult(
            createdByUser = photoResult,
            createdBySteps = cameraX.getAllPictures().map {
                PhotoResult(
                    createdAt = it,
                    filePath = it
                )
            }
        )

        requireActivity().setResult(
            RESULT_OK,
            Intent().apply {
                putExtra(RESULT_LIVENESS_CAMERAX, livenessPhotoResult)
            }
        )
        requireActivity().finish()
    }
}
