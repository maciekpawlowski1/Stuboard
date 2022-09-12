package com.pawlowski.stuboard.presentation.edit_event

import org.orbitmvi.orbit.ContainerHost

interface IEditEventViewModel: ContainerHost<EditEventUiState, EditEventSingleEvent> {
    fun moveToNextPage()
    fun moveToPreviousPage()
    fun changeTittleInput(newValue: String)
    fun changeSinceTime(newTime: Long)
    fun changeToTime(newTime: Long)
}