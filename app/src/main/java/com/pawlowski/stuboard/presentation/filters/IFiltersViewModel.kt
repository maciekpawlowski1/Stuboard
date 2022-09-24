package com.pawlowski.stuboard.presentation.filters

import kotlinx.coroutines.flow.StateFlow

interface IFiltersViewModel {
    val uiState: StateFlow<FiltersUiState>
    fun onAction(action: FiltersScreenAction)
}