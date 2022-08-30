package com.pawlowski.stuboard.domain

import com.pawlowski.stuboard.data.IFiltersDao
import com.pawlowski.stuboard.presentation.filters.FilterModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class FiltersRepository(private val filtersDao: IFiltersDao): IFiltersRepository {
    override fun getSelectedFilters(): Flow<List<FilterModel>> {
        return filtersDao.getSelectedFilters()
    }

    override fun getAllSuggestedNotSelectedFilters(): Flow<List<FilterModel>> {
        return filtersDao.getAllSuggestedNotSelectedFilters()
    }

    override suspend fun selectFilter(filterModel: FilterModel) {
        withContext(Dispatchers.IO)
        {
            filtersDao.selectFilter(filterModel)
        }
    }

    override suspend fun unselectFilter(filterModel: FilterModel) {
        withContext(Dispatchers.IO)
        {
            filtersDao.unselectFilter(filterModel)
        }
    }
}