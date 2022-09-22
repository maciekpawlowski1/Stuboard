package com.pawlowski.stuboard.ui.models

data class EventItemForPreview(
    val eventId: String = "",
    val tittle: String = "",
    val place: String = "",
    val dateDisplayString: String = "",
    val isFree: Boolean = false,
    val imageUrl: String = ""
)
