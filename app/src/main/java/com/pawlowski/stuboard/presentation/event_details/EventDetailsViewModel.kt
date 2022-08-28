package com.pawlowski.stuboard.presentation.event_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawlowski.stuboard.presentation.use_cases.GetEventDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val getEventDetailsUseCase: GetEventDetailsUseCase,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val eventId = savedStateHandle.get<String>("eventId")!!.toInt()

    private val eventResult = getEventDetailsUseCase(eventId)
    private val isRefreshing = eventResult.map { it == null || !it.isFresh }
    val uiState = combine(eventResult, isRefreshing)
    { eventResult, isRefreshing ->
        EventDetailsUiState(isRefreshing = isRefreshing, eventDetails = eventResult?.event)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = EventDetailsUiState(isRefreshing = true, eventDetails = null)
    )
}