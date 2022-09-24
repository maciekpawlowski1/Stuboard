package com.pawlowski.stuboard.presentation.event_status

import com.pawlowski.stuboard.presentation.utils.UiText

sealed interface EventStatusSingleEvent
{
    data class ShowErrorToast(val text: UiText): EventStatusSingleEvent
    object NavigateBack: EventStatusSingleEvent
    object NavigateBackToMyEvents: EventStatusSingleEvent
}