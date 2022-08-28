package com.pawlowski.stuboard.presentation.event_details

import com.pawlowski.stuboard.ui.models.EventItemWithDetails

/**
 * @param isFresh it's not fresh if it was taken from local database
 */
data class EventDetailsResult(
    val isFresh: Boolean,
    val event: EventItemWithDetails
)
