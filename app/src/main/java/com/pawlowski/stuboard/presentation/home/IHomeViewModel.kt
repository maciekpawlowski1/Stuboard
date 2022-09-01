package com.pawlowski.stuboard.presentation.home

import kotlinx.coroutines.flow.StateFlow

interface IHomeViewModel {
    val uiState: StateFlow<HomeUiState>
}