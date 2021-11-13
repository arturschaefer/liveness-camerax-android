package com.schaefer.core.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class StateViewModel<State>(initialState: State) : ViewModel() {

    private val mutableState = MutableLiveData(initialState)
    val state: LiveData<State> = mutableState

    fun setState(newState: State) {
        mutableState.value = newState
    }
}
