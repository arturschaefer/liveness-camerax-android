package com.schaefer.livenesscamerax.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.schaefer.livenesscamerax.R
import com.schaefer.livenesscamerax.core.extensions.orFalse
import com.schaefer.livenesscamerax.core.viewmodel.ReactiveViewModel
import com.schaefer.livenesscamerax.domain.logic.LivenessChecker
import com.schaefer.livenesscamerax.domain.model.FaceResult
import com.schaefer.livenesscamerax.domain.model.HeadMovement
import com.schaefer.livenesscamerax.domain.model.LivenessType
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

    private val initialState = LivenessViewState()

    private val originalRequestedSteps = MutableLiveData<LinkedList<LivenessType>>()
    private val requestedSteps = MutableLiveData<LinkedList<LivenessType>>()
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
            val face = listFaceResult.first()
            checkFaceLiveness(face)

            atLeastOneEyeIsOpen.value =
                livenessChecker.isEyeOpened(face.leftEyeOpenProbability) ||
                        livenessChecker.isEyeOpened(face.rightEyeOpenProbability)
        } else {
            // TODO put a ResourceProvider and remove the hard code strings
            requestedSteps.value = originalRequestedSteps.value
            setState(initialState.livenessMessage(resourcesProvider.getString(R.string.liveness_camerax_message_alone)))
        }
    }

    private fun checkFaceLiveness(face: FaceResult) {
        if (requestedSteps.value?.isNotEmpty().orFalse()) {
            when (requestedSteps.value?.first) {
                LivenessType.LUMINOSITY -> {
                    if (isLuminosityGood(face.luminosity)) removeCurrentStep()
                }
                LivenessType.HEAD_FRONTAL -> {
                    if (livenessChecker.detectEulerYMovement(face.headEulerAngleY) == HeadMovement.CENTER) {
                        removeCurrentStep()
                    }
                }
                LivenessType.HEAD_LEFT -> {
                    if (livenessChecker.detectEulerYMovement(face.headEulerAngleY) == HeadMovement.LEFT) {
                        removeCurrentStep()
                    }
                }
                LivenessType.HEAD_RIGHT -> {
                    if (livenessChecker.detectEulerYMovement(face.headEulerAngleY) == HeadMovement.RIGHT) {
                        removeCurrentStep()
                    }
                }
                LivenessType.HAS_SMILED -> {
                    livenessChecker.checkSmile(face.smilingProbability) {
                        hasSmiled.value = it
                    }
                    if (hasSmiled.value.orFalse()) removeCurrentStep()
                }
                LivenessType.HAS_BLINKED -> {
                    livenessChecker.checkBothEyes(
                        face.leftEyeOpenProbability,
                        face.rightEyeOpenProbability
                    ) {
                        hasBlinked.value = it
                    }
                    if (hasBlinked.value.orFalse()) removeCurrentStep()
                }
                null -> {
                    sendAction(LivenessAction.LivenessCompleted)
                }
            }
        } else {
            sendAction(LivenessAction.EnableCameraButton(true))
        }
    }

    private fun isLuminosityGood(luminosity: Float?): Boolean {
        return luminosity?.let { it > MINIMUM_LUMINOSITY }.orFalse()
    }

    fun setupSteps(validateRequested: List<LivenessType>) {
        requestedSteps.value = LinkedList<LivenessType>().apply { addAll(validateRequested) }
        originalRequestedSteps.value =
            LinkedList<LivenessType>().apply { addAll(validateRequested) }
        setState(initialState.livenessMessage(getMessage(requestedSteps.value)))
    }

    private fun removeCurrentStep() {
        requestedSteps.value?.pop()
        setState(initialState.livenessMessage(getMessage(requestedSteps.value)))
    }

    // TODO put a ResourceProvider and remove the hard code strings
    private fun getMessage(livenessType: LinkedList<LivenessType>?): String {
        if (livenessType.isNullOrEmpty()) {
            return resourcesProvider.getString(R.string.liveness_camerax_step_completed)
        }

        return when (livenessType.first) {
            LivenessType.LUMINOSITY -> {
                resourcesProvider.getString(R.string.liveness_camerax_step_luminosity)
            }
            LivenessType.HEAD_FRONTAL -> {
                resourcesProvider.getString(R.string.liveness_camerax_step_head_frontal)
            }
            LivenessType.HEAD_RIGHT -> {
                resourcesProvider.getString(R.string.liveness_camerax_step_head_left)
            }
            LivenessType.HEAD_LEFT -> {
                resourcesProvider.getString(R.string.liveness_camerax_step_head_right)
            }
            LivenessType.HAS_SMILED -> {
                resourcesProvider.getString(R.string.liveness_camerax_step_smile)
            }
            LivenessType.HAS_BLINKED -> {
                resourcesProvider.getString(R.string.liveness_camerax_step_blink_eyes)
            }
            null -> resourcesProvider.getString(R.string.liveness_camerax_step_completed)
        }
    }
}
