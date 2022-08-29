package com.pawlowski.stuboard.domain

import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.presentation.filters.FilterModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeFiltersRepository: IFiltersRepository {
    override fun getSelectedFilters(): Flow<List<FilterModel>> = flow {
        val defaultEmit = listOf(
            FilterModel.Category("Naukowe", R.drawable.learning_category_image),
            FilterModel.EntryPrice.Free,
            FilterModel.Place.Online
        )
        delay(20)
        emit(defaultEmit)
    }
}