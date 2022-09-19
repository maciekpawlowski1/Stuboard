package com.pawlowski.stuboard.presentation.edit_event

import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.data.mappers.CategoryHandler
import com.pawlowski.stuboard.data.mappers.EditEventInitialCategories
import com.pawlowski.stuboard.data.mappers.OrganisationHandler
import com.pawlowski.stuboard.presentation.filters.FilterModel
import com.pawlowski.stuboard.presentation.filters.FilterType
import com.pawlowski.stuboard.presentation.use_cases.RestoreEditEventStateUseCase
import com.pawlowski.stuboard.presentation.use_cases.SaveEditingEventUseCase
import com.pawlowski.stuboard.ui.event_editing.EditEventScreenType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class EditEventViewModel @Inject constructor(
    private val geocoder: Geocoder,
    private val saveEditingEventUseCase: SaveEditingEventUseCase,
    private val restoreEditEventStateUseCase: RestoreEditEventStateUseCase,
    private val savedStateHandle: SavedStateHandle,
): IEditEventViewModel, ViewModel() {

    val eventId: Int? = savedStateHandle.get<String>("editEventId")?.let {
        if(it == "new")
            null
        else
            it
    }?.toInt()

    override val container: Container<EditEventUiState, EditEventSingleEvent> = container(
        EditEventUiState(
            categories = initialCategories(),
            suggestedOrganisations = initialExistingOrganisations()
        )
    )

    private val positionUpdatesFlow = MutableSharedFlow<Unit>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun moveToNextPage() = intent {
        reduce {
            state.copy(currentPage = when(state.currentPage) {
                EditEventScreenType.FIRST -> { EditEventScreenType.SECOND }
                EditEventScreenType.SECOND -> { EditEventScreenType.THIRD }
                EditEventScreenType.THIRD -> { EditEventScreenType.FOURTH }
                EditEventScreenType.FOURTH -> { EditEventScreenType.FIFTH }
                else -> {state.currentPage}
            })
        }
        emitNewPositionRefreshInQueue()
        saveEvent()
    }

    override fun moveToPreviousPage() = intent {
        if(state.currentPage == EditEventScreenType.FIRST)
        {
            postSideEffect(EditEventSingleEvent.NavigateBack)
        }
        else
        {
            reduce {
                state.copy(currentPage = when(state.currentPage) {
                    EditEventScreenType.SECOND -> { EditEventScreenType.FIRST }
                    EditEventScreenType.THIRD -> { EditEventScreenType.SECOND }
                    EditEventScreenType.FOURTH -> { EditEventScreenType.THIRD }
                    EditEventScreenType.FIFTH -> { EditEventScreenType.FOURTH }
                    else -> {state.currentPage}
                })
            }
            emitNewPositionRefreshInQueue()
        }

        saveEvent()
    }

    override fun changeTittleInput(newValue: String) = intent {
        reduce {
            state.copy(tittleInput = newValue)
        }
    }

    override fun changeSinceTime(newTime: Long) = intent {
        reduce {
            state.copy(sinceTime = newTime)
        }
    }

    override fun changeToTime(newTime: Long) = intent {
        reduce {
            state.copy(toTime = newTime)
        }
    }

    override fun changeIsOnline(isOnline: Boolean) = intent {
        reduce {
            state.copy(isOnline= isOnline)
        }
    }

    override fun changeImageUri(imageUri: String) = intent {
        reduce {
            state.copy(imageUrl = imageUri)
        }
    }

    override fun changeCategorySelection(category: FilterModel, isSelected: Boolean) = intent {
        reduce {
            val newCategories = state.categories.toMutableMap().apply {
                val newMap = get(category.filterType)?.toMutableMap()?.apply {
                    set(category, isSelected)
                }
                set(category.filterType, newMap?: mapOf())
            }
            state.copy(categories = newCategories,
            markerDrawableRes = if(category is FilterModel.Category)
            {
                //TODO: Check error if nothing selected
                val mainCategory = newCategories[FilterType.CATEGORY]?.filter { it.value }?.keys?.firstOrNull()
                mainCategory?.let {
                    (mainCategory as FilterModel.Category).markerDrawableId
                }?: R.drawable.concert_marker_icon

            }
            else
                state.markerDrawableRes
            )
        }
    }

    override fun changeCityInput(newValue: String) = intent {
        reduce {
            state.copy(city = newValue)
        }
        emitNewPositionRefreshInQueue()
    }

    override fun changeStreetInput(newValue: String) = intent {
        reduce {
            state.copy(streetAndNumber = newValue)
        }
        emitNewPositionRefreshInQueue()
    }

    override fun changeCountryInput(newValue: String) = intent {
        reduce {
            state.copy(country = newValue)
        }
        emitNewPositionRefreshInQueue()
    }

    override fun changePlaceNameInput(newValue: String) = intent {
        reduce {
            state.copy(placeName = newValue)
        }
    }

    override fun changeOrganisationSearchInput(newValue: String) = intent {
        reduce {
            state.copy(
                organisationSearchText = newValue,
                suggestedOrganisations = initialExistingOrganisations().filter { newValue.trim().isEmpty() || it.tittle.lowercase().contains(newValue.trim().lowercase()) }
            )
        }
    }

    override fun changeSelectedOrganisation(organisation: Organisation) = intent {
        reduce {
            state.copy(selectedOrganisation = organisation)
        }
    }

    override fun changeDescriptionInput(newValue: String) = intent {
        reduce {
            state.copy(
                description = newValue,
            )
        }
    }

    override fun changeSiteInput(newValue: String) = intent {
        reduce {
            state.copy(
                site = newValue,
            )
        }
    }

    override fun changeFacebookSiteInput(newValue: String) = intent {
        reduce {
            state.copy(
                facebookSite = newValue,
            )
        }
    }

    private fun handlePositionUpdates() = intent(registerIdling = false) {
        repeatOnSubscription {
            positionUpdatesFlow.collectLatest {
                val currentState = state
                val address = getPositionFromAddress(currentState.streetAndNumber, currentState.city, currentState.country)
                reduce {
                    address?.let {
                        state.copy(positionOnMap = LatLng(it.latitude, it.longitude))
                    }?: kotlin.run {
                        state.copy(positionOnMap = null)
                    }
                }
            }
        }
    }

    private fun emitNewPositionRefreshInQueue()
    {
        positionUpdatesFlow.tryEmit(Unit)
    }

    override fun validateAndMoveToPublishing() = intent {
        //TODO: Validate
        postSideEffect(EditEventSingleEvent.NavigateToPublishing)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun getPositionFromAddress(street: String, city: String, country: String): Address?
    {
        return if(street.isNotEmpty() && city.isNotEmpty() && country.isNotEmpty())
        {
            val locationName = "$street $city, $country"
            val address = withContext(Dispatchers.IO) {
                return@withContext try {
                    geocoder.getFromLocationName(locationName, 1).firstOrNull()
                }
                catch (e: Exception)
                {
                    null
                }
            }
            address

        }
        else
            null
    }

    private fun initialCategories(): Map<FilterType, Map<FilterModel, Boolean>>
    {
        return EditEventInitialCategories.initialCategories

    }

    private fun initialExistingOrganisations(): List<Organisation.Existing>
    {
        return OrganisationHandler.getAllExistingOrganisations()
    }

    private fun saveNewOrRestoreEvent() = intent {
        eventId?.let {
            val restoredState = restoreEditEventStateUseCase(it.toLong())
            reduce {
                restoredState
            }

        }?: kotlin.run {
            val id = saveEditingEventUseCase(EditEventUiState())
            reduce {
                state.copy(eventId = id.toInt())
            }
        }
    }

    private fun saveEvent() = intent {
        if(state.eventId != 0)
        {
            saveEditingEventUseCase(state)
        }
    }

    init {
        saveNewOrRestoreEvent()
        handlePositionUpdates()
    }

}
