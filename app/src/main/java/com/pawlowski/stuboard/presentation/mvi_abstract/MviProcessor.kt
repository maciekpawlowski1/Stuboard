package com.pawlowski.stuboard.presentation.mvi_abstract

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class MviProcessor<S: State, I: Intent, E: SingleEvent>: ViewModel()
{
    private val _intent: MutableSharedFlow<I> = MutableSharedFlow()

    private val _viewState: MutableStateFlow<S> by lazy {
        MutableStateFlow(initialState)
    }

    private val _singleEvent: MutableSharedFlow<E> = MutableSharedFlow()

    val viewState: StateFlow<S>
        get() = _viewState.asStateFlow()

    val singleEvent: Flow<E> = _singleEvent

    fun sendIntent(intent: I) {
        viewModelScope.launch {
            _intent.emit(intent)
        }
    }

    protected fun triggerSingleEvent(singleEvent: E)
    {
        viewModelScope.launch {
            _singleEvent.emit(singleEvent)
        }
    }

    protected abstract fun initialState(): S

    protected abstract val reducer: Reducer<S, I>

    protected abstract suspend fun handleIntent(intent: I, state: S): I?

    private val initialState: S by lazy {
        initialState()
    }

    interface Reducer<S, I> {
        fun reduce(state: S, intent: I): S
    }

    init {
        subscribeToIntents()
    }

    private fun subscribeToIntents() {
        viewModelScope.launch {
            _intent.collect { intent ->
                reduceInternal(_viewState.value, intent)
                launch {
                    handleIntent(intent, _viewState.value)?.let { newIntent ->
                        sendIntent(newIntent)
                    }
                }
            }
        }
    }

    private fun reduceInternal(prevState: S, intent: I) {
        val newState = reducer.reduce(prevState, intent)
        _viewState.update { newState }
    }
}