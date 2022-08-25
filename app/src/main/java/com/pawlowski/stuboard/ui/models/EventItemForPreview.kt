package com.pawlowski.stuboard.ui.models

data class EventItemForPreview(
                     val eventId: Int,
                     val tittle: String,
                     val place: String,
                     val dateDisplayString: String,
                     val isFree: Boolean,
                     val imageUrl: String
)
