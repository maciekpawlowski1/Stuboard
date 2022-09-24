package com.pawlowski.stuboard.presentation.my_events

import com.pawlowski.stuboard.ui.models.EventItemForPreview

sealed class MyEventsUiState {
    object Loading : MyEventsUiState()
    data class Success(
        val events: Map<EventItemForPreview, EventPublishState>
    ): MyEventsUiState()
}