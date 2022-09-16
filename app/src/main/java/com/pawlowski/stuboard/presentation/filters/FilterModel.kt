package com.pawlowski.stuboard.presentation.filters

import java.text.DecimalFormat

sealed class FilterModel(val tittle: String, val filterType: FilterType)
{
    data class Category(private val _tittle: String,
                        val categoryId: Int,
                        val realImageDrawableId: Int,
                        val iconDrawableId: Int,
                        val markerDrawableId: Int,
                        val selectedMarkerDrawableId: Int
                        ): FilterModel(_tittle, FilterType.CATEGORY)

    sealed class Place(tittle: String): FilterModel(tittle, FilterType.PLACE)
    {
        object Online: Place(tittle = "Online")
        data class RealPlace(val city: String): Place(tittle = city)
    }

    sealed class EntryPrice(tittle: String): FilterModel(tittle, FilterType.ENTRY_PRICE)
    {
        object Free: EntryPrice(tittle = "Za darmo")
        object Paid: EntryPrice(tittle = "Płatne")
        data class MaxPrice(val maxPrice: Double): EntryPrice("<${DecimalFormat("#.##").format(maxPrice)} zł")
    }

    sealed class Other(tittle: String): FilterModel(tittle, FilterType.OTHER)
    {
        object Inside: Other("W środku")
        object Outside: Other("Na zewnątrz")

    }

    sealed class Access(tittle: String): FilterModel(tittle, FilterType.ACCESS)
    {
        object EVERYBODY: Access("Dla wszystkich")
        object PROTECTED: Access("Ograniczony")
    }

    sealed class Registration(tittle: String): FilterModel(tittle, FilterType.REGISTRATION)
    {
        object NoRegistrationNeeded: Registration("Bez rejestracji")
        object RegistrationNeeded: Registration("Wymaga rejestracji")
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
    CUSTOM_TEXT(5, "Wyszukiwanie tekstowe"),
    OTHER(6, "Inne"),
    ACCESS(7, "Dostęp"),
    REGISTRATION(8, "Rejestracja")
}

