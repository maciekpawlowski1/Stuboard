package com.pawlowski.stuboard.presentation.home

import com.pawlowski.stuboard.presentation.filters.FilterModel
import com.pawlowski.stuboard.ui.models.EventItemForPreview

data class HomeEventTypeSuggestion(
    val suggestionType: String,
    val suggestionFilters: List<FilterModel>? = null,
    val isLoading: Boolean,
    val events: List<EventItemForPreview>
)
