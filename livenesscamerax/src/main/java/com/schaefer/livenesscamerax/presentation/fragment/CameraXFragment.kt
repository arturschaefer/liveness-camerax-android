package com.schaefer.livenesscamerax.presentation.fragment

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.schaefer.livenesscamerax.BuildConfig
import com.schaefer.livenesscamerax.R
import com.schaefer.livenesscamerax.camera.CameraX
import com.schaefer.livenesscamerax.camera.CameraXImpl
import com.schaefer.livenesscamerax.camera.callback.CameraXCallback
import com.schaefer.livenesscamerax.camera.callback.CameraXCallbackImpl
import com.schaefer.livenesscamerax.camera.provider.file.FileHandler
import com.schaefer.livenesscamerax.camera.provider.image.ImageHandler
import com.schaefer.livenesscamerax.core.exceptions.LivenessCameraXException
import com.schaefer.livenesscamerax.core.extensions.observeOnce
import com.schaefer.livenesscamerax.core.extensions.orFalse
import com.schaefer.livenesscamerax.core.extensions.shouldShowRequest
import com.schaefer.livenesscamerax.core.extensions.snack
import com.schaefer.livenesscamerax.databinding.LivenessCameraxFragmentBinding
import com.schaefer.livenesscamerax.di.LibraryModule.container
import com.schaefer.livenesscamerax.domain.checker.LivenessChecker
import com.schaefer.livenesscamerax.presentation.model.CameraSettings
import com.schaefer.livenesscamerax.presentation.model.PhotoResult
import com.schaefer.livenesscamerax.presentation.navigation.EXTRAS_LIVENESS_CAMERA_SETTINGS
import com.schaefer.livenesscamerax.presentation.provider.resource.ResourcesProvider
import com.schaefer.livenesscamerax.presentation.provider.result.ResultHandler
import com.schaefer.livenesscamerax.presentation.viewmodel.LivenessViewModel
import com.schaefer.livenesscamerax.presentation.viewmodel.LivenessViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import timber.log.Timber

@ExperimentalCoroutinesApi
@FlowPreview
@InternalCoroutinesApi
internal class CameraXFragment : Fragment(R.layout.liveness_camerax_fragment) {

    private var _binding: LivenessCameraxFragmentBinding? = null
    private val binding get() = _binding!!

    private val cameraSettings: CameraSettings by lazy {
        activity?.intent?.extras?.getParcelable(
            EXTRAS_LIVENESS_CAMERA_SETTINGS
        ) ?: CameraSettings()
    }
    private val resourceProvider: ResourcesProvider by lazy { container.resourceProvider() }
    private val livenessChecker: LivenessChecker by lazy { container.provideLivenessChecker() }
    private val resultHandler: ResultHandler by lazy { container.provideResultHandler() }
    private val imageHandler: ImageHandler by lazy { container.provideImageHandler() }
    private val fileHandler: FileHandler by lazy {
        container.provideFileHandler(cameraSettings.storageType)
    }
    private val livenessViewModel: LivenessViewModel by viewModels {
        LivenessViewModelFactory(resourceProvider, livenessChecker)
    }

    private val cameraXCallback: CameraXCallback by lazy {
        CameraXCallbackImpl(
            ::handlePictureSuccess,
            resultHandler::error,
            imageHandler,
        )
    }

    private val cameraX: CameraX by lazy {
        CameraXImpl(
            settings = cameraSettings,
            cameraXCallback = cameraXCallback,
            lifecycleOwner = this,
            fileHandler = fileHandler
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

        livenessViewModel.setupSteps(cameraSettings.livenessStepList)
        cameraPermission.launch(cameraManifest)
    }

    override fun onStop() {
        super.onStop()

        if (!requireActivity().isFinishing) {
            resultHandler.error(LivenessCameraXException.ContextSwitchException())
            requireActivity().finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!BuildConfig.DEBUG) fileHandler.deleteStorageFiles()
    }

    private fun permissionIsGranted() {
        startCamera()
        startObservers()
    }

    private fun startObservers() {
        lifecycle.addObserver(cameraX.getLifecycleObserver())

        livenessViewModel.state.observe(viewLifecycleOwner) { state ->
            binding.tvStepText.text = state.messageLiveness
            binding.cameraCaptureButton.isVisible = state.isButtonEnabled
        }

        livenessViewModel.apply {
            observeFacesDetection(cameraX.getFacesFlowable())
            observeLuminosity(cameraX.getLuminosity())
            hasBlinked.observeOnce(viewLifecycleOwner) { takePicture(it) }
            hasSmiled.observeOnce(viewLifecycleOwner) { takePicture(it) }
            hasGoodLuminosity.observeOnce(viewLifecycleOwner) { takePicture(it) }
            hasHeadMovedLeft.observeOnce(viewLifecycleOwner) { takePicture(it) }
            hasHeadMovedRight.observeOnce(viewLifecycleOwner) { takePicture(it) }
            hasHeadMovedCenter.observeOnce(viewLifecycleOwner) { takePicture(it) }
        }
    }

    private fun takePicture(requestPicture: Boolean) {
        if (requestPicture) cameraX.takePicture(false)
    }

    private fun startCamera() {
        cameraX.startCamera(binding.viewFinder)

        binding.cameraCaptureButton.setOnClickListener {
            cameraX.takePicture(true)
        }

        binding.overlayView.apply {
            init()
            invalidate()
            isVisible = true
        }

        binding.tvStepText.isVisible = true
    }

    private fun handlePictureSuccess(photoResult: PhotoResult, takenByUser: Boolean) {
        if (takenByUser) {
            val filesPath = cameraX.getAllPictures()
            resultHandler.success(photoResult, filesPath)
            requireActivity().finish()
        } else {
            Timber.d(photoResult.toString())
        }
    }
}
