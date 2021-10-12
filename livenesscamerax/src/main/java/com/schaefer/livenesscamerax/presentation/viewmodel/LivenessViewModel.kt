package com.schaefer.livenesscamerax.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.schaefer.livenesscamerax.R
import com.schaefer.livenesscamerax.core.extensions.orFalse
import com.schaefer.livenesscamerax.core.viewmodel.ReactiveViewModel
import com.schaefer.livenesscamerax.domain.logic.LivenessChecker
import com.schaefer.livenesscamerax.domain.model.FaceResult
import com.schaefer.livenesscamerax.domain.model.HeadMovement
import com.schaefer.livenesscamerax.domain.model.StepLiveness
import com.schaefer.livenesscamerax.presentation.provider.ResourcesProvider
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.LinkedList

private const val MINIMUM_LUMINOSITY = 100

@InternalCoroutinesApi
internal class LivenessViewModel(
    private val resourcesProvider: ResourcesProvider,
    private val livenessChecker: LivenessChecker,
) : ReactiveViewModel<LivenessViewState, LivenessAction>(LivenessViewState()) {
    // UI State
    private val _state = LivenessViewState()

    // Steps
    private var originalRequestedSteps = LinkedList<StepLiveness>()
    private var requestedSteps = LinkedList<StepLiveness>()

    // Faces
    private val facesMutable = MutableLiveData<List<FaceResult>>()
    private val moreThanOneFace = MutableLiveData<Boolean>()
    private val atLeastOneEyeIsOpen = MutableLiveData<Boolean>()
    private val hasBlinked = MutableLiveData<Boolean>()
    private val hasSmiled = MutableLiveData<Boolean>()

    // TODO handle error
    fun observeFacesDetection(facesFlowable: Flow<List<FaceResult>>) {
        viewModelScope.launch {
            facesFlowable.collect {
                handleFaces(it)
            }
        }
    }

    fun observeLuminosity(luminosity: Flow<Double>) {
        viewModelScope.launch {
            luminosity.collect {
                // TODO need to decide if it's necessary
                Timber.tag("Luminosity").d(it.toString())
            }
        }
    }

    private fun handleFaces(listFaceResult: List<FaceResult>) {
        facesMutable.value = listFaceResult
        moreThanOneFace.value = livenessChecker.hasMoreThanOneFace(listFaceResult)

        if (livenessChecker.hasMoreThanOneFace(listFaceResult).not()) {
            setState(_state.livenessMessage(getMessage()))

            val face = listFaceResult.first()
            checkFaceLiveness(face)

            //TODO handle accessibility of people with no eye
            atLeastOneEyeIsOpen.value = livenessChecker.validateAtLeastOneEyeIsOpen(face)
        } else {
            requestedSteps.clear()
            requestedSteps.addAll(originalRequestedSteps)

            setState(_state.livenessError(resourcesProvider.getString(R.string.liveness_camerax_message_alone)))
        }
    }

    private fun checkFaceLiveness(face: FaceResult) {
        if (requestedSteps.isNotEmpty().orFalse()) {

            when (requestedSteps.first) {
                StepLiveness.STEP_LUMINOSITY -> {
                    if (isLuminosityGood(face.luminosity)) {
                        removeCurrentStep()
                    }
                }
                StepLiveness.STEP_HEAD_FRONTAL -> {
                    if (livenessChecker.detectEulerYMovement(face.headEulerAngleY) == HeadMovement.CENTER) {
                        removeCurrentStep()
                    }
                }
                StepLiveness.STEP_HEAD_LEFT -> {
                    livenessChecker.validateHeadMovement(face, HeadMovement.LEFT) {
                        removeCurrentStep()
                    }
                }
                StepLiveness.STEP_HEAD_RIGHT -> {
                    livenessChecker.validateHeadMovement(face, HeadMovement.RIGHT) {
                        removeCurrentStep()
                    }
                }
                StepLiveness.STEP_SMILE -> {
                    livenessChecker.checkSmile(face.smilingProbability) {
                        removeCurrentStep()
                        hasSmiled.value = it
                    }
                }
                StepLiveness.STEP_BLINK -> {
                    livenessChecker.checkBothEyes(
                        face.leftEyeOpenProbability,
                        face.rightEyeOpenProbability
                    ) {
                        hasBlinked.value = it
                        removeCurrentStep()
                    }
                }
                null -> {
                    setState(_state.stepsCompleted(getMessage()))
                }
            }
        } else {
            setState(_state.stepsCompleted(getMessage()))
        }
    }

    private fun isLuminosityGood(luminosity: Float?): Boolean {
        return luminosity?.let { it > MINIMUM_LUMINOSITY }.orFalse()
    }

    fun setupSteps(validateRequested: List<StepLiveness>) {
        requestedSteps = LinkedList<StepLiveness>().apply { addAll(validateRequested) }
        originalRequestedSteps = LinkedList<StepLiveness>().apply { addAll(validateRequested) }
        setState(_state.livenessMessage(getMessage()))
    }

    private fun removeCurrentStep() {
        // TODO get picture after complete each step
        requestedSteps.pop()
        setState(_state.livenessMessage(getMessage()))
    }

    private fun getMessage(): String {
        requestedSteps.let {
            if (it.isNullOrEmpty()) {
                return resourcesProvider.getString(R.string.liveness_camerax_step_completed)
            }

            return when (it.first) {
                StepLiveness.STEP_LUMINOSITY -> {
                    resourcesProvider.getString(R.string.liveness_camerax_step_luminosity)
                }
                StepLiveness.STEP_HEAD_FRONTAL -> {
                    resourcesProvider.getString(R.string.liveness_camerax_step_head_frontal)
                }
                StepLiveness.STEP_HEAD_RIGHT -> {
                    resourcesProvider.getString(R.string.liveness_camerax_step_head_left)
                }
                StepLiveness.STEP_HEAD_LEFT -> {
                    resourcesProvider.getString(R.string.liveness_camerax_step_head_right)
                }
                StepLiveness.STEP_SMILE -> {
                    resourcesProvider.getString(R.string.liveness_camerax_step_smile)
                }
                StepLiveness.STEP_BLINK -> {
                    resourcesProvider.getString(R.string.liveness_camerax_step_blink_eyes)
                }
                null -> resourcesProvider.getString(R.string.liveness_camerax_step_completed)
            }
        }
    }
}
