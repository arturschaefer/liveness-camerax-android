package com.schaefer.livenesscamerax.presentation.viewmodel

import com.schaefer.livenesscamerax.core.viewmodel.UIState

internal data class LivenessViewState(val messageLiveness: String = "") : UIState {
    fun livenessMessage(message: String) = this.copy(messageLiveness = message)
}
