package com.pawlowski.stuboard.presentation.my_events

sealed interface MyEventsSingleEvent {
    object NavigateToAddNewEvent: MyEventsSingleEvent

}