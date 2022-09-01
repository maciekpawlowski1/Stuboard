package com.pawlowski.stuboard.domain

import com.pawlowski.stuboard.presentation.filters.FilterModel
import com.pawlowski.stuboard.ui.models.CategoryItem
import com.pawlowski.stuboard.ui.utils.PreviewUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakePreferencesRepositoryImpl: PreferencesRepository {
    override fun getCategoriesInPreferredOrder(): Flow<List<FilterModel.Category>> = flow {
        emit(listOf())
        delay(20)
        emit(PreviewUtils.defaultFilters.filterIsInstance<FilterModel.Category>())
    }

}