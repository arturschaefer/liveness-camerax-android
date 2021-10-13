package com.schaefer.livenesscamerax.core.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

internal open class StateViewModel<State>(initialState: State) : ViewModel() {

    private val mutableState = MutableLiveData(initialState)
    val state: LiveData<State> = mutableState

    internal fun setState(newState: State) {
        mutableState.value = newState
    }
}
