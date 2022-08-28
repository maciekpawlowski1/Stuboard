package com.pawlowski.stuboard.presentation.home

import com.pawlowski.stuboard.ui.models.CategoryItem

data class HomeUiState(
    val eventsSuggestions: List<HomeEventTypeSuggestion>,
    val preferredCategories: List<CategoryItem>
)
