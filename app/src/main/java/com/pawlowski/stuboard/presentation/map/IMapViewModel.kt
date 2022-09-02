package com.pawlowski.stuboard.presentation.map

import kotlinx.coroutines.flow.StateFlow

interface IMapViewModel {
    val uiState: StateFlow<MapUiState>
}