package com.pawlowski.stuboard.ui.utils

import com.google.android.gms.maps.model.LatLng
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.ui.models.*

object PreviewUtils {
    val defaultEventPreviews = listOf(
        EventItemForPreview(
            eventId = 1,
            tittle = "Juwenalia Krakowskie 2022", place = "Kraków, Rostafińskiego 38",
            dateDisplayString = "28 września - 30 września", isFree = false,
            imageUrl = "https://images.unsplash.com/photo-1429962714451-bb934ecdc4ec?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2070&q=80"
        ),

        EventItemForPreview(
            eventId = 2,
            tittle = "Warsztaty Bazy Danych AGH MySQL", place = "Online",
            dateDisplayString = "8 października 17:00", isFree = true,
            imageUrl = "https://images.unsplash.com/photo-1633412802994-5c058f151b66?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1480&q=80"
        ),

        EventItemForPreview(
            eventId = 3,
            tittle = "Neural networks workshop", place = "Online",
            dateDisplayString = "8 października 17:00", isFree = true,
            imageUrl = "https://images.unsplash.com/photo-1604869515882-4d10fa4b0492?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2070&q=80"
        ),
    )

    val defaultEventItemsForMap = listOf(
        EventItemForMapScreen(
            eventId = 1,
            position = LatLng(50.0601, 19.9438),
            tittle = "Juwenalia Krakowskie 2022", place = "Kraków, Rostafińskiego 38",
            dateDisplayString = "28 września - 30 września", isFree = false,
            imageUrl = "https://images.unsplash.com/photo-1429962714451-bb934ecdc4ec?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2070&q=80",
            mainCategoryDrawableId = R.drawable.concert_marker_icon,
            mainCategoryDrawableIdWhenSelected = R.drawable.concert_selected_marker_icon,
            ),

        EventItemForMapScreen(
            eventId = 2,
            position = LatLng(50.0621, 19.9394),
            tittle = "Warsztaty Bazy Danych AGH MySQL", place = "Online",
            dateDisplayString = "8 października 17:00", isFree = true,
            imageUrl = "https://images.unsplash.com/photo-1633412802994-5c058f151b66?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1480&q=80",
            mainCategoryDrawableId = R.drawable.naukowe_marker_icon,
            mainCategoryDrawableIdWhenSelected = R.drawable.naukowe_selected_marker_icon,
        ),

        EventItemForMapScreen(
            eventId = 3,
            position = LatLng(50.0722, 19.9461),
            tittle = "Neural networks workshop", place = "Kraków, Rynkowa 12",
            dateDisplayString = "8 października 17:00", isFree = true,
            imageUrl = "https://images.unsplash.com/photo-1604869515882-4d10fa4b0492?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2070&q=80",
            mainCategoryDrawableId = R.drawable.naukowe_marker_icon,
            mainCategoryDrawableIdWhenSelected = R.drawable.naukowe_selected_marker_icon,
        ),
    )

    val defaultMarkers = listOf(
        EventMarker(
            LatLng(50.0601, 19.9438),
            R.drawable.concert_marker_icon,
            "Juwenalia Krakowskie 2022"
        ),
        EventMarker(
            LatLng(50.0621, 19.9394),
            R.drawable.naukowe_marker_icon,
            "Warsztaty Tworzenia Aplikacji Mobilnych"
        ),
        EventMarker(
            LatLng(50.0722, 19.9461),
            R.drawable.concert_selected_marker_icon,
            "Koncert Wielkanocny"
        ),
    )

    val defaultFilters = listOf(
        "Kraków", "Ten tydzień", "Naukowe", "Koncerty", "Na zewnątrz"
    )

    val defaultFullEvent = EventItemWithDetails(
        tittle = "Neural networks workshop with Will Smith and Google AI Engineer Allan Walker",
        dateDisplay = "środa, 5 czerwca 2022",
        hourDisplay = "18:00 - 21:00 CEST",
        place = "Wydarzenie odbywa się online",
        categoriesDrawablesId = listOf(R.drawable.sports_category_icon),
        price = 0.0f,
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc vulputate libero et velit interdum, ac aliquet odio mattis. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Curabitur tempus urna at turpis condimentum lobortis. Ut commodo efficitur neque. Ut diam quam, semper iaculis condimentum ac, vestibulum eu nisl.\u2028Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc vulputate libero et velit interdum, ac aliquet odio mattis. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Curabitur tempus urna at turpis condimentum lobortis. Ut commodo efficitur neque. Ut diam quam, semper iaculis condimentum ac, vestibulum eu nisl",
        imageUrl = "https://images.unsplash.com/photo-1604869515882-4d10fa4b0492?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2070&q=80",
        organisation = OrganisationItemForPreview(
            tittle = "Akademia Górniczo-Hutnicza w Krakowie",
            "https://mecc20.pl/wp-content/uploads/2020/01/Logo-AGH-1.jpg"
        )
    )
}