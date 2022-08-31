package com.pawlowski.stuboard.presentation.search

import kotlinx.coroutines.flow.StateFlow

interface ISearchViewModel {
    val uiState: StateFlow<SearchUiState>
}