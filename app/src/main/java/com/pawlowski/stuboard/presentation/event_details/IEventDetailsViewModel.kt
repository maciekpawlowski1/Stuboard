package com.pawlowski.stuboard.presentation.event_details

import kotlinx.coroutines.flow.StateFlow

interface IEventDetailsViewModel {
    val uiState: StateFlow<EventDetailsUiState>
    fun deleteClick() = run { }
}