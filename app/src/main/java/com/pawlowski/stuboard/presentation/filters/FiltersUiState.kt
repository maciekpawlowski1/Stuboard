package com.pawlowski.stuboard.presentation.filters

data class FiltersUiState(
    val searchText: String,
    val selectedFilters: List<FilterModel>,
    val suggestedFilters: Map<FilterType, List<FilterModel>>,
    val editTextVisible: Boolean
)
