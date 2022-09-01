package com.pawlowski.stuboard.data

import com.pawlowski.stuboard.presentation.filters.FilterModel
import kotlinx.coroutines.flow.Flow

interface IFiltersDao {
    suspend fun selectFilter(filterModel: FilterModel)
    suspend fun unselectFilter(filterModel: FilterModel)
    suspend fun unselectAllFilters()
    fun getSelectedFilters(): Flow<List<FilterModel>>
    fun getSelectedFiltersCount(): Flow<Int>
    fun getAllSuggestedNotSelectedFilters(): Flow<List<FilterModel>>
}