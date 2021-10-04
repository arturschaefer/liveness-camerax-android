package com.schaefer.livenesscamerax.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schaefer.livenesscamerax.core.extensions.orFalse
import com.schaefer.livenesscamerax.domain.model.FaceResult
import com.schaefer.livenesscamerax.domain.model.HeadMovement
import com.schaefer.livenesscamerax.domain.model.LivenessType
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

private const val EYE_OPENED_PROBABILITY = 0.4F
private const val IS_SMILING_PROBABILITY = 0.3F
private const val EULER_Y_RIGHT_MOVEMENT = 35
private const val EULER_Y_LEFT_MOVEMENT = -35
private const val MINIMUM_LUMINOSITY = 100

@InternalCoroutinesApi
internal class LivenessViewModel : ViewModel() {

    private val originalRequestedSteps = MutableLiveData<LinkedList<LivenessType>>()
    private val requestedSteps = MutableLiveData<LinkedList<LivenessType>>()
    private val facesMutable = MutableLiveData<List<FaceResult>>()
    private val moreThanOneFace = MutableLiveData<Boolean>()
    private val atLeastOneEyeIsOpen = MutableLiveData<Boolean>()
    private val hasBlinked = MutableLiveData<Boolean>()
    private val hasSmiled = MutableLiveData<Boolean>()

    //TODO handle error
    fun observeFacesDetection(facesFlowable: Flow<List<FaceResult>>) {
        viewModelScope.launch {
            facesFlowable.collect {
                handleFaces(it)
            }
        }
    }

    fun observeLuminosity(luminosity: Flow<Double>){
        viewModelScope.launch {
            luminosity.collect {
                Timber.tag("Luminosity").d(it.toString())
            }
        }
    }
    private fun handleFaces(listFaceResult: List<FaceResult>) {
        facesMutable.value = listFaceResult
        moreThanOneFace.value = hasMoreThanOneFace(listFaceResult)

        if (hasMoreThanOneFace(listFaceResult).not()) {
            val face = listFaceResult.first()
            if (requestedSteps.value?.isNotEmpty().orFalse()) {
                when (requestedSteps.value?.first) {
                    LivenessType.LUMINOSITY -> {
                        if (isLuminosityGood(face.luminosity)) removeCurrentStep()
                    }
                    LivenessType.HEAD_FRONTAL -> {
                        if (detectEulerYMovement(face.headEulerAngleY) == HeadMovement.CENTER) {
                            removeCurrentStep()
                        }
                    }
                    LivenessType.HEAD_LEFT -> {
                        if (detectEulerYMovement(face.headEulerAngleY) == HeadMovement.LEFT) {
                            removeCurrentStep()
                        }
                    }
                    LivenessType.HEAD_RIGHT -> {
                        if (detectEulerYMovement(face.headEulerAngleY) == HeadMovement.RIGHT) {
                            removeCurrentStep()
                        }
                    }
                    LivenessType.HAS_SMILED -> {
                        checkSmile(face.smilingProbability)
                        if (hasSmiled.value.orFalse()) removeCurrentStep()
                    }
                    LivenessType.HAS_BLINKED -> {
                        checkBothEyes(face.leftEyeOpenProbability, face.rightEyeOpenProbability)
                        if (hasBlinked.value.orFalse()) removeCurrentStep()
                    }
                    null -> {
//                        sendAction(LivenessAction.LivenessCompleted)
                    }
                }
            } else {
//                sendAction(LivenessAction.EnableCameraButton(true))
            }

            atLeastOneEyeIsOpen.value =
                isEyeOpened(face.leftEyeOpenProbability) ||
                        isEyeOpened(face.rightEyeOpenProbability)
        } else {
            //TODO put a ResourceProvider and remove the hard code strings
            requestedSteps.value = originalRequestedSteps.value
//            setState(initialState.livenessMessage("Você está sozinho?"))
        }
    }

    private fun hasMoreThanOneFace(listFaceResult: List<FaceResult>) =
        listFaceResult.isNotEmpty() && listFaceResult.size > 1

    private fun isEyeOpened(eyeOpenedProbabilityValue: Float?): Boolean {
        return eyeOpenedProbabilityValue?.let { (it > EYE_OPENED_PROBABILITY) }.orFalse()
    }

    private fun checkBothEyes(
        leftEyeProbability: Float?,
        rightEyeProbability: Float?
    ): Boolean {
        return (leftEyeProbability?.let { it > EYE_OPENED_PROBABILITY }.orFalse() &&
                rightEyeProbability?.let { it > EYE_OPENED_PROBABILITY }.orFalse()).also {
            if (it.not()) hasBlinked.value = true
        }
    }

    private fun checkSmile(smilingProbability: Float?): Boolean {
        return smilingProbability?.let { it > IS_SMILING_PROBABILITY }.orFalse().also { isSmiling ->
            if (isSmiling) hasSmiled.value = isSmiling
        }
    }

    private fun detectEulerYMovement(
        headEulerAngleY: Float
    ): HeadMovement {
        return when {
            headEulerAngleY > EULER_Y_RIGHT_MOVEMENT -> HeadMovement.RIGHT
            headEulerAngleY < EULER_Y_LEFT_MOVEMENT -> HeadMovement.LEFT
            else -> HeadMovement.CENTER
        }
    }

    private fun isLuminosityGood(luminosity: Float?): Boolean {
        return luminosity?.let { it > MINIMUM_LUMINOSITY }.orFalse()
    }

    fun setupSteps(validateRequested: List<LivenessType>) {
        requestedSteps.value = LinkedList<LivenessType>().apply { addAll(validateRequested) }
        originalRequestedSteps.value =
            LinkedList<LivenessType>().apply { addAll(validateRequested) }
//        setState(initialState.livenessMessage(getMessage(requestedSteps.value)))
    }

    private fun removeCurrentStep() {
        requestedSteps.value?.pop()
//        setState(initialState.livenessMessage(getMessage(requestedSteps.value)))
    }

    //TODO put a ResourceProvider and remove the hard code strings
    private fun getMessage(livenessType: LinkedList<LivenessType>?): String {
        if (livenessType.isNullOrEmpty()) {
            return "Parabéns. Você conclui as etapas! Tire uma foto!"
        }

        return when (livenessType.first) {
            LivenessType.LUMINOSITY -> "Verifique a iluminação da foto"
            LivenessType.HEAD_FRONTAL -> "Mantenha sua cabeça na frente da câmera"
            LivenessType.HEAD_RIGHT -> "Vire a cabeça para a esquerda"
            LivenessType.HEAD_LEFT -> "Vire a cabeça para a direita"
            LivenessType.HAS_SMILED -> "Sorria"
            LivenessType.HAS_BLINKED -> "Pisque os olhos"
            null -> "Parabéns. Você conclui as etapas!"
        }
    }
}