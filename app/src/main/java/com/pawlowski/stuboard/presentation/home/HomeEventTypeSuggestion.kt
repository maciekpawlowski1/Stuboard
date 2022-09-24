package com.pawlowski.stuboard.presentation.home

import com.pawlowski.stuboard.presentation.filters.FilterModel
import com.pawlowski.stuboard.ui.models.PreviewEventHolder

data class HomeEventTypeSuggestion(
    val suggestionType: String,
    val suggestionFilters: List<FilterModel>? = null,
    val isLoading: Boolean,
    val events: List<PreviewEventHolder>
)
