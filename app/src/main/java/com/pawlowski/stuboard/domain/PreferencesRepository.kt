package com.pawlowski.stuboard.domain

import com.pawlowski.stuboard.data.mappers.CategoryHandler
import com.pawlowski.stuboard.presentation.filters.FilterModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PreferencesRepository @Inject constructor(): IPreferencesRepository {
    override fun getCategoriesInPreferredOrder(): Flow<List<FilterModel.Category>> = flow {
        val emitValue = listOf(
            CategoryHandler.getCategoryById(1),
            CategoryHandler.getCategoryById(2),
            CategoryHandler.getCategoryById(3),
            CategoryHandler.getCategoryById(4),
        )
        emit(emitValue)
        //TODO: get order which user has chosen
    }
}