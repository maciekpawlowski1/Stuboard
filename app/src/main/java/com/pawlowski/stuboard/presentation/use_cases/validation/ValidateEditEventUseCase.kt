package com.pawlowski.stuboard.presentation.use_cases.validation

import com.pawlowski.stuboard.presentation.edit_event.EditEventUiState
import com.pawlowski.stuboard.presentation.filters.FilterModel
import com.pawlowski.stuboard.presentation.filters.FilterType
import com.pawlowski.stuboard.presentation.utils.UiText
import com.pawlowski.stuboard.ui.event_editing.EditEventScreenType
import javax.inject.Inject

class ValidateEditEventUseCase @Inject constructor() {
    operator fun invoke(editEventUiState: EditEventUiState): Pair<ValidationResult, EditEventScreenType?>
    {
        val tittle = editEventUiState.tittleInput.trim()
        return if(tittle.isEmpty())
        {
            Pair(ValidationResult(false, UiText.StaticText("Tytuł wydarzenia jest wymagany!")), EditEventScreenType.FIRST)
        }
        else if(tittle.length < 5)
        {
            Pair(ValidationResult(false, UiText.StaticText("Tytuł wydarzenia jest zbyt krótki!")), EditEventScreenType.FIRST)
        }
        else if(editEventUiState.sinceTime == null)
        {
            Pair(ValidationResult(false, UiText.StaticText("Data rozpoczęcia wydarzenia jest wymagana!")), EditEventScreenType.FIRST)
        }
        else if(editEventUiState.toTime == null)
        {
            Pair(ValidationResult(false, UiText.StaticText("Data zakończenia wydarzenia jest wymagana!")), EditEventScreenType.FIRST)
        }
        else if(editEventUiState.sinceTime > editEventUiState.toTime)
        {
            Pair(ValidationResult(false, UiText.StaticText("Data zakończenia wydarzenia musi być po dacie rozpoczęcia wydarzenia!")), EditEventScreenType.FIRST)
        }
        else if(editEventUiState.imageUrl == null || editEventUiState.imageUrl.isEmpty())
        {
            Pair(ValidationResult(false, UiText.StaticText("Zdjęcie wydarzenia jest wymagane!")), EditEventScreenType.FIRST)
        }
        else if(!editEventUiState.categories[FilterType.CATEGORY].isSomethingSelected())
        {
            Pair(ValidationResult(false, UiText.StaticText("Przynajmniej jedna główna kategoria jest wymagana!")), EditEventScreenType.SECOND)
        }
/*        else if(!editEventUiState.categories[FilterType.ACCESS].isSomethingSelected())
        {
            Pair(ValidationResult(false, UiText.StaticText("Nie wybrano elementu w kategorii dostępu!")), EditEventScreenType.SECOND)
        }*/
        else if(!editEventUiState.categories[FilterType.REGISTRATION].isSomethingSelected())
        {
            Pair(ValidationResult(false, UiText.StaticText("Nie wybrano elementu w kategorii rejestracja!")), EditEventScreenType.SECOND)
        }
        else if(!editEventUiState.categories[FilterType.ENTRY_PRICE].isSomethingSelected())
        {
            Pair(ValidationResult(false, UiText.StaticText("Nie wybrano elementu w kategorii cena!")), EditEventScreenType.SECOND)
        }
        else if(!editEventUiState.isOnline && editEventUiState.city.trim().isEmpty())
        {
            Pair(ValidationResult(false, UiText.StaticText("Pole miasto jest wymagane!")), EditEventScreenType.THIRD)
        }
        else if(!editEventUiState.isOnline && editEventUiState.streetAndNumber.trim().isEmpty())
        {
            Pair(ValidationResult(false, UiText.StaticText("Pole ulica jest wymagane!")), EditEventScreenType.THIRD)
        }
        else if(!editEventUiState.isOnline && editEventUiState.country.trim().isEmpty())
        {
            Pair(ValidationResult(false, UiText.StaticText("Pole kraj jest wymagane!")), EditEventScreenType.THIRD)
        }
        else if(!editEventUiState.isOnline && editEventUiState.positionOnMap == null)
        {
            Pair(ValidationResult(false, UiText.StaticText("Nie wykryto poprawnie położenia na mapie. Spróbuj zmienić pola adresu wydarzenia!")), EditEventScreenType.THIRD)
        }
        else if(editEventUiState.selectedOrganisation == null)
        {
            Pair(ValidationResult(false, UiText.StaticText("Organizator wydarzenia jest wymagany")), EditEventScreenType.FOURTH)
        }
        else if(editEventUiState.description.trim().isEmpty())
        {
            Pair(ValidationResult(false, UiText.StaticText("Opis wydarzenia jest wymagany!")), EditEventScreenType.FIFTH)
        }
        else
        {
            Pair(ValidationResult(true), null)
        }
    }

    private fun Map<FilterModel, Boolean>?.isSomethingSelected(): Boolean
    {
        return !this?.filter { it.value }.isNullOrEmpty()
    }
}


