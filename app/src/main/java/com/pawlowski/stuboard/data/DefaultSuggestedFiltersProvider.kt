package com.pawlowski.stuboard.data

import com.pawlowski.stuboard.data.mappers.CategoryHandler
import com.pawlowski.stuboard.presentation.filters.FilterModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultSuggestedFiltersProvider @Inject constructor(): ISuggestedFiltersProvider {
    override fun getSuggestedFilters(): List<FilterModel> {
        return listOf(
            //Categories
            CategoryHandler.getCategoryById(1),
            CategoryHandler.getCategoryById(2),
            CategoryHandler.getCategoryById(3),
            CategoryHandler.getCategoryById(4),
            //Price
            FilterModel.EntryPrice.Free,
            FilterModel.EntryPrice.Paid,
            //Places
            FilterModel.Place.Online,
            FilterModel.Place.RealPlace("Kraków"),
            FilterModel.Place.RealPlace("Katowice"),
            FilterModel.Place.RealPlace("Wrocław"),
            FilterModel.Place.RealPlace("Warszawa"),
            //Time
            FilterModel.Time.MaxTimeFilter.Today,
            FilterModel.Time.MaxTimeFilter.Next2Days,
            FilterModel.Time.MaxTimeFilter.Next7Days,
            FilterModel.Time.MaxTimeFilter.Next14Days,
            FilterModel.Time.MaxTimeFilter.Next30Days
        )
    }

}