package com.pawlowski.stuboard.presentation.home

import com.pawlowski.stuboard.presentation.filters.FilterModel

sealed class HomeUiAction {
    data class ClearAllFiltersAndSelectFilters(val filters: List<FilterModel>?): HomeUiAction()
}
