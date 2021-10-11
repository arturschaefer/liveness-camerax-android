package com.schaefer.livenesscamerax.core.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

internal open class ReactiveViewModel<State, Action>(initialState: State): ViewModel() {

    private val mutableState = MutableLiveData(initialState)
    val state: LiveData<State> = mutableState

    private val mutableAction = MutableLiveData<Action>()
    val action: LiveData<Action> = mutableAction

    internal fun setState(newState: State) {
        mutableState.value = newState
    }

    internal fun sendAction(newAction: Action) {
        mutableAction.value = newAction!!
    }
}
