package com.pawlowski.stuboard.presentation.map

import com.pawlowski.stuboard.presentation.filters.FilterModel
import com.pawlowski.stuboard.ui.models.EventItemForPreviewWithLocation
import com.pawlowski.stuboard.ui.models.EventMarker

sealed class MapUiState(
    val currentFilters: List<FilterModel>
) {
    data class Loading(private val _currentFilters: List<FilterModel>): MapUiState(_currentFilters)
    data class Success(
        val events: List<EventItemForPreviewWithLocation>,
        val selectedEventId: String = "-1",
        val markers: List<EventMarker> = listOf(),
        private val _currentFilters: List<FilterModel>
    ): MapUiState(_currentFilters)
}
