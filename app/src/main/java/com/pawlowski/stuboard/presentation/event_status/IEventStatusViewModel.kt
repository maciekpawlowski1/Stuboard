package com.pawlowski.stuboard.presentation.event_status

import org.orbitmvi.orbit.ContainerHost

interface IEventStatusViewModel: ContainerHost<EventStatusUiState, EventStatusSingleEvent> {
    fun publishEvent()
}