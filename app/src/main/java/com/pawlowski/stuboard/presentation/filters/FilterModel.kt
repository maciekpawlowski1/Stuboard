package com.pawlowski.stuboard.presentation.filters

import java.text.DecimalFormat

sealed class FilterModel(val tittle: String, val filterType: FilterType)
{
    data class Category(private val _tittle: String, val categoryDrawable: Int): FilterModel(_tittle, FilterType.CATEGORY)

    sealed class Place(private val _tittle: String): FilterModel(_tittle, FilterType.PLACE)
    {
        object Online: Place(_tittle = "Online")
        data class RealPlace(val city: String): Place(_tittle = city)
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

enum class FilterType(val filterTypeId: Int, val typeString: String) {
    CATEGORY(1, "Kategorie"),
    PLACE(2, "Miejsce"),
    ENTRY_PRICE(3, "Cena"),
    TIME(4, "Czas"),
    CUSTOM_TEXT(5, "Wyszukiwanie tekstowe")
}

