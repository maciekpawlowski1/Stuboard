package com.pawlowski.stuboard.presentation.filters

import java.text.DecimalFormat

sealed class FilterModel(tittle: String, filterType: FilterType)
{
    data class Category(val tittle: String, val categoryDrawable: Int): FilterModel(tittle, FilterType.CATEGORY)

    sealed class Place(val tittle: String): FilterModel(tittle, FilterType.PLACE)
    {
        object Online: Place(tittle = "Online")
        data class RealPlace(val city: String): Place(tittle = city)
    }

    sealed class EntryPrice(tittle: String): FilterModel(tittle, FilterType.ENTRY_PRICE)
    {
        object Free: EntryPrice(tittle = "Za darmo")
        data class MaxPrice(val maxPrice: Double): EntryPrice("<${DecimalFormat("#.##").format(maxPrice)} zł")
    }

    sealed class Time(tittle: String)
    {
        sealed class MaxTimeFilter() //TODO

        data class CustomFromToTimeFilter(val fromTimestamp: Long?, val toTimestamp: Long?): Time(
            convertToString(fromTimestamp, toTimestamp))
        {
            companion object {
                fun convertToString(fromTimestamp: Long?, toTimestamp: Long?): String
                {
                    TODO("NOT IMPLEMENTED YET")
                }
            }
        }
    }

    data class CustomTextFilter(val customText: String): FilterModel(customText, FilterType.CUSTOM_TEXT)

}

enum class FilterType(filterTypeId: Int) {
    CATEGORY(1),
    PLACE(2),
    ENTRY_PRICE(3),
    TIME(4),
    CUSTOM_TEXT(5)
}

