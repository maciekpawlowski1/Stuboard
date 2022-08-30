package com.pawlowski.stuboard.data

import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.presentation.filters.FilterModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultSuggestedFiltersProvider @Inject constructor(): ISuggestedFiltersProvider {
    override fun getSuggestedFilters(): List<FilterModel> {
        return listOf(
            //Categories
            FilterModel.Category("Naukowe", R.drawable.learning_category_image),
            FilterModel.Category("Koncerty", R.drawable.concerts_category_image),
            FilterModel.Category("Sportowe", R.drawable.sports_category_image),
            FilterModel.Category("Biznesowe", R.drawable.learning_category_image), //TODO: Change image
            //Price
            FilterModel.EntryPrice.MaxPrice(25.0),
            FilterModel.EntryPrice.MaxPrice(50.0),
            FilterModel.EntryPrice.MaxPrice(100.0),
            FilterModel.EntryPrice.MaxPrice(200.0),
            //Places
            FilterModel.Place.Online,
            FilterModel.Place.RealPlace("Kraków"),
            FilterModel.Place.RealPlace("Katowice"),
            FilterModel.Place.RealPlace("Wrocław"),
            FilterModel.Place.RealPlace("Warszawa"),
        )
    }

}