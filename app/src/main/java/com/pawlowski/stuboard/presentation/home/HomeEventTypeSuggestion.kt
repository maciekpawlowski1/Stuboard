package com.pawlowski.stuboard.presentation.home

import com.pawlowski.stuboard.ui.models.EventItemForPreview

data class HomeEventTypeSuggestion(
    val suggestionType: String,
    val isLoading: Boolean,
    val events: List<EventItemForPreview>
)
