package com.pawlowski.stuboard.data.mappers

import com.pawlowski.stuboard.presentation.filters.FilterModel
import com.pawlowski.stuboard.presentation.filters.FilterType

object EditEventInitialCategories {
    val initialCategories: Map<FilterType, Map<FilterModel, Boolean>>
    get() {
        return mapOf(
            Pair(FilterType.CATEGORY, mapOf(
                Pair(CategoryHandler.getCategoryById(1), false),
                Pair(CategoryHandler.getCategoryById(2), false),
                Pair(CategoryHandler.getCategoryById(3), false),
                Pair(CategoryHandler.getCategoryById(4), false),
            )),
/*            Pair(FilterType.ACCESS, mapOf(
                Pair(FilterModel.Access.EVERYBODY, false),
                Pair(FilterModel.Access.PROTECTED, false)
            )),*/
            Pair(FilterType.REGISTRATION, mapOf(
                Pair(FilterModel.Registration.NoRegistrationNeeded, false),
                Pair(FilterModel.Registration.RegistrationNeeded, false)
            )),
            Pair(FilterType.ENTRY_PRICE, mapOf(
                Pair(FilterModel.EntryPrice.Free, false),
                Pair(FilterModel.EntryPrice.Paid,false)
            )),
/*            Pair(FilterType.OTHER, mapOf(
                Pair(FilterModel.Other.Outside, false),
                Pair(FilterModel.Other.Inside, false)
            ))*/

        )
    }
}