package com.pawlowski.stuboard.presentation.edit_event

sealed interface EditEventSingleEvent
{
    object NavigateToPublishing: EditEventSingleEvent
}