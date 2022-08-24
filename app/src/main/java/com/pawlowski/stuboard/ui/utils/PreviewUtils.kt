package com.pawlowski.stuboard.ui.utils

import com.pawlowski.stuboard.ui.models.EventItemForPreview

object PreviewUtils {
    val defaultEventPreviews = listOf(
        EventItemForPreview(tittle = "Juwenalia Krakowskie 2022", place = "Kraków, Rostafińskiego 38",
            dateDisplayString = "28 września - 30 września", isFree = false,
            imageUrl = "https://images.unsplash.com/photo-1429962714451-bb934ecdc4ec?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2070&q=80"),

        EventItemForPreview(tittle = "Warsztaty Bazy Danych AGH MySQL", place = "Online",
            dateDisplayString = "8 października 17:00", isFree = true,
            imageUrl = "https://images.unsplash.com/photo-1633412802994-5c058f151b66?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1480&q=80"),

        EventItemForPreview(tittle = "Neural networks workshop", place = "Online",
            dateDisplayString = "8 października 17:00", isFree = true,
            imageUrl = "https://images.unsplash.com/photo-1604869515882-4d10fa4b0492?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2070&q=80"),
    )
}