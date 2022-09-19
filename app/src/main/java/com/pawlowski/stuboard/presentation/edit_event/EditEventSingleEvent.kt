package com.pawlowski.stuboard.presentation.edit_event

sealed interface EditEventSingleEvent
{
    data class NavigateToPublishing(val eventId: Int): EditEventSingleEvent
    object NavigateBack: EditEventSingleEvent
}