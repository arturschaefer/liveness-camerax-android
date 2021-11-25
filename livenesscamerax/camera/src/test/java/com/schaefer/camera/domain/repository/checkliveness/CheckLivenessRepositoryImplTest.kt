package com.schaefer.camera.domain.repository.checkliveness

import com.schaefer.camera.domain.model.FaceResult
import com.schaefer.domain.model.HeadMovement
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

internal class CheckLivenessRepositoryImplTest {

    private val checkLivenessRepository = CheckLivenessRepositoryFactory.create()

    @Test
    fun `isEyeOpened should return true with high probability`() {
        val eyeProbability = 0.5f

        val result = checkLivenessRepository.isEyeOpened(eyeProbability)

        assert(result)
    }

    @Test
    fun `isEyeOpened should return false with low probability`() {
        val eyeProbability = 0.34f

        val result = checkLivenessRepository.isEyeOpened(eyeProbability)

        assertFalse(result)
    }

    @Test
    fun `checkBothEyes should return true when both eyes are opened`() {
        val leftEyeProbability = 0.5f
        val rightEyeProbability = 0.5f

        val result = checkLivenessRepository.checkBothEyes(
            leftEyeProbability,
            rightEyeProbability,
            mockk()
        )

        assert(result)
    }

    @Test
    fun `checkBothEyes should return false and call the callback when both eyes are closed`() {
        val leftEyeProbability = 0.2f
        val rightEyeProbability = 0.2f
        val callbackBlinked = mockk<(Boolean) -> Unit>()
        every { callbackBlinked.invoke(any()) } returns Unit

        val result = checkLivenessRepository.checkBothEyes(
            leftEyeProbability,
            rightEyeProbability,
            callbackBlinked
        )

        assertFalse(result)
        verify { callbackBlinked.invoke(true) }
    }

    @Test
    fun `hasOneSingleFace should return true when the list is greater than one`() {
        val faceList = listOf<FaceResult>(mockk(), mockk())

        val result = checkLivenessRepository.hasMoreThanOneFace(faceList)

        assert(result)
    }

    @Test
    fun `hasOneSingleFace should return false with a empty list`() {
        val faceList = listOf<FaceResult>()

        val result = checkLivenessRepository.hasMoreThanOneFace(faceList)

        assertFalse(result)
    }

    @Test
    fun `hasOneSingleFace should return true with a single item`() {
        val faceList = listOf<FaceResult>(mockk())

        val result = checkLivenessRepository.hasMoreThanOneFace(faceList)

        assertFalse(result)
    }

    @Test
    fun `checkSmile should return true when smile probability is high`() {
        val smileProbability = 0.4f
        val callbackSmiled = mockk<(Boolean) -> Unit>()
        every { callbackSmiled(any()) } returns Unit

        val result = checkLivenessRepository.checkSmile(smileProbability, callbackSmiled)

        assert(result)
        verify { callbackSmiled(true) }
    }

    @Test
    fun `checkSmile should return false when smile probability is low`() {
        val smileProbability = 0.2f

        val result = checkLivenessRepository.checkSmile(smileProbability, mockk())

        assertFalse(result)
    }

    @Test
    fun `detectEulerYMovement should return get head movement left`() {
        val headEulerAngleY = -40f

        val result = checkLivenessRepository.detectEulerYMovement(headEulerAngleY)

        assert(result == HeadMovement.LEFT)
    }

    @Test
    fun `detectEulerYMovement should return get head movement right`() {
        val headEulerAngleY = 40f

        val result = checkLivenessRepository.detectEulerYMovement(headEulerAngleY)

        assert(result == HeadMovement.RIGHT)
    }

    @Test
    fun `detectEulerYMovement should return get head movement center`() {
        val headEulerAngleY = 20f

        val result = checkLivenessRepository.detectEulerYMovement(headEulerAngleY)

        assert(result == HeadMovement.CENTER)
    }

    @Test
    fun `validateHeadMovement should call removeCurrentStep when head movement is center`() {
        val face = mockk<FaceResult>()
        every { face.headEulerAngleY } returns 20f
        val headMovement = HeadMovement.CENTER
        val removeCurrentStep = mockk<(Boolean) -> Unit>()
        every { removeCurrentStep(any()) } returns Unit

        checkLivenessRepository.validateHeadMovement(face, headMovement, removeCurrentStep)

        verify { removeCurrentStep(true) }
    }

    @Test
    fun `validateHeadMovement should call removeCurrentStep when head movement is left`() {
        val face = mockk<FaceResult>()
        every { face.headEulerAngleY } returns -40f
        val headMovement = HeadMovement.LEFT
        val removeCurrentStep = mockk<(Boolean) -> Unit>()
        every { removeCurrentStep(any()) } returns Unit

        checkLivenessRepository.validateHeadMovement(face, headMovement, removeCurrentStep)

        verify { removeCurrentStep(true) }
    }

    @Test
    fun `validateHeadMovement should call removeCurrentStep when head movement is right`() {
        val face = mockk<FaceResult>()
        every { face.headEulerAngleY } returns 40f
        val headMovement = HeadMovement.RIGHT
        val removeCurrentStep = mockk<(Boolean) -> Unit>()
        every { removeCurrentStep(any()) } returns Unit

        checkLivenessRepository.validateHeadMovement(face, headMovement, removeCurrentStep)

        verify { removeCurrentStep(true) }
    }

    @Test
    fun `validateAtLeastOneEyeIsOpen should return true when left eye is open`() {
        val face = mockk<FaceResult>()
        every { face.leftEyeOpenProbability } returns 0.5f
        every { face.rightEyeOpenProbability } returns 0.1f

        val result = checkLivenessRepository.validateAtLeastOneEyeIsOpen(face)

        assert(result)
    }

    @Test
    fun `validateAtLeastOneEyeIsOpen should return false when both eyes are closed`() {
        val face = mockk<FaceResult>()
        every { face.leftEyeOpenProbability } returns 0.2f
        every { face.rightEyeOpenProbability } returns 0.1f

        val result = checkLivenessRepository.validateAtLeastOneEyeIsOpen(face)

        assertFalse(result)
    }
}
