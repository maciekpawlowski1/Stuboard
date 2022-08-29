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

    override fun getAllSuggestedNotSelectedFilters(): Flow<List<FilterModel>> = flow {
        val defaultEmit = listOf(
            FilterModel.Category("Koncerty", R.drawable.concerts_category_image),
            FilterModel.Category("Sportowe", R.drawable.sports_category_image),
            FilterModel.Category("Biznesowe", R.drawable.learning_category_image),
            FilterModel.EntryPrice.MaxPrice(25.0),
            FilterModel.EntryPrice.MaxPrice(50.0),
            FilterModel.EntryPrice.MaxPrice(100.0),
            FilterModel.EntryPrice.MaxPrice(200.0),
            FilterModel.Place.RealPlace("Kraków"),
            FilterModel.Place.RealPlace("Katowice"),
            FilterModel.Place.RealPlace("Warszawa"),
        )
        delay(20)
        emit(defaultEmit)
    }
}