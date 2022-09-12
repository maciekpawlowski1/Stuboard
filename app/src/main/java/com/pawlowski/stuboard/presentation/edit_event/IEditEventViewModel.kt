package com.pawlowski.stuboard.presentation.edit_event

import org.orbitmvi.orbit.ContainerHost

interface IEditEventViewModel: ContainerHost<EditEventUiState, EditEventSingleEvent> {
    fun moveToNextPage()
    fun moveToPreviousPage()
}