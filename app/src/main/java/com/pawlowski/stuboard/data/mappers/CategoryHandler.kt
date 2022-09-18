package com.pawlowski.stuboard.data.mappers

import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.presentation.filters.FilterModel

object CategoryHandler {
    fun getCategoryById(categoryId: Int): FilterModel.Category {
        return when(categoryId) {
            1 -> FilterModel.Category(
                "Koncerty",
                categoryId,
                R.drawable.concerts_category_image,
                R.drawable.guitar_icon, R.drawable.concert_marker_icon,
                R.drawable.concert_selected_marker_icon,
            )
            3-> FilterModel.Category(
                "Naukowe",
                categoryId,
                R.drawable.learning_category_image,
                R.drawable.learning_icon, R.drawable.naukowe_marker_icon,
                R.drawable.naukowe_selected_marker_icon,
            )
            4 -> FilterModel.Category(
                "Sportowe",
                categoryId,
                R.drawable.sports_category_image,
                R.drawable.sports_category_icon, R.drawable.sports_marker_icon,
                R.drawable.sports_selected_marker_icon,
            )
            2 -> FilterModel.Category(
                "Biznesowe",
                categoryId,
                R.drawable.business_category_image,
                R.drawable.business_icon, R.drawable.business_marker_icon,
                R.drawable.business_selected_marker_icon,
            )
            else -> FilterModel.Category(
                "Nieznana kategoria",
                categoryId,
                R.drawable.concerts_category_image,
                R.drawable.guitar_icon, R.drawable.concert_marker_icon,
                R.drawable.concert_selected_marker_icon,
            )
        }
    }
}