package com.pawlowski.stuboard.presentation.home

import com.pawlowski.stuboard.presentation.filters.FilterModel

data class HomeUiState(
    val eventsSuggestions: List<HomeEventTypeSuggestion>,
    val preferredCategories: List<FilterModel.Category>
)
