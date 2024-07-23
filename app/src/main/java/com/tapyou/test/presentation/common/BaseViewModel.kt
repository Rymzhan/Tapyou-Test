package com.tapyou.test.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

abstract class BaseViewModel<State : BaseState, Effect : BaseEffect> : ViewModel() {

    private val initState: State by lazy { createInitialState() }

    abstract fun createInitialState(): State

    protected val currState: State
        get() = uiState.value

    private val _uiState: MutableStateFlow<State> = MutableStateFlow(initState)
    val uiState = _uiState.asStateFlow()

    private val _action: MutableSharedFlow<BaseAction> = MutableSharedFlow()
    private val action = _action.asSharedFlow()

    init {
        subscribeActions()
    }

    private fun subscribeActions() {
        viewModelScope.launch {
            action.collect(::processAction)
        }
    }

    private var state by Delegates.observable(initState) { _, oldValue, newValue ->
        if (oldValue == newValue)
            return@observable

        viewModelScope.launch {
            _uiState.update {
                newValue
            }
        }
    }

    fun sendAction(action: BaseAction) {
        val newAction = action
        viewModelScope.launch { _action.emit(newAction) }
    }

    protected abstract fun processAction(action: BaseAction)

    protected fun setState(reduce: State.() -> State) {
        state = currState.reduce()
    }
}
