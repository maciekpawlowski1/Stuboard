package com.pawlowski.stuboard.presentation.edit_event

import com.pawlowski.stuboard.presentation.utils.UiText

sealed interface EditEventSingleEvent
{
    data class NavigateToPublishing(val eventId: Int): EditEventSingleEvent
    data class ShowErrorToast(val text: UiText): EditEventSingleEvent
    object NavigateBack: EditEventSingleEvent
}