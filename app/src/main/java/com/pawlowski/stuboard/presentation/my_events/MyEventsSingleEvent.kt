package com.pawlowski.stuboard.presentation.my_events

import com.pawlowski.stuboard.presentation.utils.UiText

sealed interface MyEventsSingleEvent {
    object NavigateBack: MyEventsSingleEvent
    data class NavigateToEditEvent(val eventId: String): MyEventsSingleEvent
    data class NavigateToEventPreview(val eventId: String): MyEventsSingleEvent
    data class ShowErrorToast(val text: UiText): MyEventsSingleEvent
}