package com.pawlowski.stuboard.presentation.map

import org.orbitmvi.orbit.ContainerHost

interface IMapMviViewModel: ContainerHost<MapUiState, MapSingleEvent> {
    fun onPageChanged(index: Int)
    fun onEventSelected(eventId: Int)
}