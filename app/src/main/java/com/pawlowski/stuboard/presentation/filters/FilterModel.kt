package com.pawlowski.stuboard.presentation.filters

import com.pawlowski.stuboard.data.mappers.dateFormatter
import java.time.OffsetDateTime

sealed class FilterModel(open val tittle: String, val filterType: FilterType)
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

    sealed class Time(tittle: String): FilterModel(tittle, FilterType.TIME)
    {
        sealed class MaxTimeFilter(override val tittle: String, val maxTime: OffsetDateTime): Time(tittle)
        {
            object Today: MaxTimeFilter("Dzisiaj", OffsetDateTime.now().plusDays(1))
            object Next2Days: MaxTimeFilter("Następne 2 dni", OffsetDateTime.now().plusDays(2))
            object Next7Days: MaxTimeFilter("Ten tydzień", OffsetDateTime.now().plusDays(7))
            object Next14Days: MaxTimeFilter("Następne 2 tygodnie", OffsetDateTime.now().plusDays(14))
            object Next30Days: MaxTimeFilter("Następne 30 dni", OffsetDateTime.now().plusDays(30))
        }

        data class CustomFromToTimeFilter(val fromTimestamp: OffsetDateTime?, val toTimestamp: OffsetDateTime?): Time(
            convertToString(fromTimestamp, toTimestamp))
        {
            companion object {
                fun convertToString(fromTimestamp: OffsetDateTime?, toTimestamp: OffsetDateTime?): String
                {
                    val fromString = fromTimestamp?.format(dateFormatter())
                    val toString = toTimestamp?.format(dateFormatter())
                    val middle = if(fromString == null || toTimestamp == null)
                        ""
                    else
                        " - "
                    return "$fromString$middle$toString"
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

