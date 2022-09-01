package com.pawlowski.stuboard.presentation.search

import androidx.paging.PagingData
import com.pawlowski.stuboard.presentation.filters.FilterModel
import com.pawlowski.stuboard.ui.models.EventItemForPreview

data class SearchUiState(
    val selectedFilters: List<FilterModel>,
)
