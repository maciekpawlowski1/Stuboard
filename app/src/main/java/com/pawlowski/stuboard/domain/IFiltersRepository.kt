package com.pawlowski.stuboard.domain

import com.pawlowski.stuboard.presentation.filters.FilterModel
import kotlinx.coroutines.flow.Flow

interface IFiltersRepository {
    fun getSelectedFilters(): Flow<List<FilterModel>>
    fun getAllSuggestedNotSelectedFilters(): Flow<List<FilterModel>>
    suspend fun selectFilter(filterModel: FilterModel)
    suspend fun unselectFilter(filterModel: FilterModel)
}