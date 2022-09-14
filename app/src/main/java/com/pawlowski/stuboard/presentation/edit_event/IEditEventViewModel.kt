package com.pawlowski.stuboard.presentation.edit_event

import com.pawlowski.stuboard.presentation.filters.FilterModel
import org.orbitmvi.orbit.ContainerHost

interface IEditEventViewModel: ContainerHost<EditEventUiState, EditEventSingleEvent> {
    fun moveToNextPage()
    fun moveToPreviousPage()
    fun changeTittleInput(newValue: String)
    fun changeSinceTime(newTime: Long)
    fun changeToTime(newTime: Long)
    fun changeCategorySelection(category: FilterModel, isSelected: Boolean)
    fun changeIsOnline(isOnline: Boolean)
    fun changeCountryInput(newValue: String)
    fun changeStreetInput(newValue: String)
    fun changeCityInput(newValue: String)
    fun changePlaceNameInput(newValue: String)
    fun changeOrganisationSearchInput(newValue: String)
}