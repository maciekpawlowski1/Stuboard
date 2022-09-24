package com.pawlowski.stuboard.presentation.event_details

import com.pawlowski.stuboard.ui.models.EventItemWithDetails

data class EventDetailsUiState(
    val isRefreshing: Boolean = false,
    val eventDetails: EventItemWithDetails? = null
)
