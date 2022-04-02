package com.schaefer.livenesscamerax.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.schaefer.camera.domain.model.FaceResult
import com.schaefer.core.extensions.orFalse
import com.schaefer.core.resourceprovider.ResourcesProvider
import com.schaefer.core.viewmodel.StateViewModel
import com.schaefer.domain.model.HeadMovement
import com.schaefer.domain.repository.LivenessRepository
import com.schaefer.livenesscamerax.R
import com.schaefer.livenesscamerax.domain.model.StepLiveness
import com.schaefer.livenesscamerax.domain.model.toDomain
import com.schaefer.livenesscamerax.domain.usecase.GetStepMessageUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.LinkedList

private const val MINIMUM_LUMINOSITY = 100

internal class LivenessViewModel(
    private val resourcesProvider: ResourcesProvider,
    private val livenessRepository: LivenessRepository<FaceResult>,
    private val getStepMessageUseCase: GetStepMessageUseCase,
) : StateViewModel<LivenessViewState>(LivenessViewState()) {
    // UI State
    private val _state = LivenessViewState()

    // Steps
    private var originalRequestedSteps = LinkedList<StepLiveness>()
    private var requestedSteps = LinkedList<StepLiveness>()

    // Faces
    private var facesMutable = listOf<FaceResult>()
    private var isFacesDetectedCorrect = false
    private val hasBlinkedMutable = MutableLiveData<Boolean>()
    val hasBlinked: LiveData<Boolean> = hasBlinkedMutable
    private val hasSmiledMutable = MutableLiveData<Boolean>()
    val hasSmiled: LiveData<Boolean> = hasSmiledMutable
    private val luminosityMutable = MutableLiveData<Boolean>()
    val hasGoodLuminosity: LiveData<Boolean> = luminosityMutable
    private val headMovementLeftMutable = MutableLiveData<Boolean>()
    val hasHeadMovedLeft: LiveData<Boolean> = headMovementLeftMutable
    private val headMovementRightMutable = MutableLiveData<Boolean>()
    val hasHeadMovedRight: LiveData<Boolean> = headMovementRightMutable
    private val headMovementCenterMutable = MutableLiveData<Boolean>()
    val hasHeadMovedCenter: LiveData<Boolean> = headMovementCenterMutable

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
        facesMutable = listFaceResult
        isFacesDetectedCorrect = livenessRepository.isFacesDetectedCorrect(listFaceResult)

        if (!isFacesDetectedCorrect) {
            setState(_state.livenessMessage(getMessage()))
            val face = listFaceResult.first()
            checkFaceLiveness(face)
        } else {
            requestedSteps.apply {
                clear()
                addAll(originalRequestedSteps)
            }
            setState(_state.livenessError(resourcesProvider.getString(R.string.liveness_camerax_message_alone)))
        }
    }

    private fun checkFaceLiveness(face: FaceResult) {
        if (requestedSteps.isNotEmpty().orFalse()) {

            when (requestedSteps.first) {
                StepLiveness.STEP_LUMINOSITY -> {
                    if (isLuminosityGood(face.luminosity)) {
                        removeCurrentStep()
                        luminosityMutable.value = true
                    }
                }
                StepLiveness.STEP_HEAD_FRONTAL -> {
                    if (livenessRepository.detectEulerYMovement(face.headEulerAngleY) == HeadMovement.CENTER) {
                        removeCurrentStep()
                        headMovementCenterMutable.value = true
                    }
                }
                StepLiveness.STEP_HEAD_LEFT -> {
                    livenessRepository.validateHeadMovement(face, HeadMovement.LEFT) {
                        removeCurrentStep()
                        headMovementLeftMutable.value = it
                    }
                }
                StepLiveness.STEP_HEAD_RIGHT -> {
                    livenessRepository.validateHeadMovement(face, HeadMovement.RIGHT) {
                        removeCurrentStep()
                        headMovementRightMutable.value = it
                    }
                }
                StepLiveness.STEP_SMILE -> {
                    livenessRepository.checkSmile(face.smilingProbability) {
                        removeCurrentStep()
                        hasSmiledMutable.value = true
                    }
                }
                StepLiveness.STEP_BLINK -> {
                    livenessRepository.validateBlinkedEyes(
                        face.leftEyeOpenProbability,
                        face.rightEyeOpenProbability
                    ) {
                        removeCurrentStep()
                        hasBlinkedMutable.value = it
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
        requestedSteps.pop()
        setState(_state.livenessMessage(getMessage()))
    }

    private fun getMessage(): String {
        return getStepMessageUseCase(requestedSteps.map { it.toDomain() })
    }
}
