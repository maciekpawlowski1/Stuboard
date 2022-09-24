package com.pawlowski.stuboard.data

import com.pawlowski.stuboard.presentation.filters.FilterModel

interface ISuggestedFiltersProvider {
    fun getSuggestedFilters(): List<FilterModel>
}