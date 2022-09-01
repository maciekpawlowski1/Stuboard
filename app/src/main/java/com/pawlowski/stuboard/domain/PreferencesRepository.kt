package com.pawlowski.stuboard.domain

import com.pawlowski.stuboard.presentation.filters.FilterModel
import com.pawlowski.stuboard.ui.models.CategoryItem
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

    fun getCategoriesInPreferredOrder(): Flow<List<FilterModel.Category>>

}