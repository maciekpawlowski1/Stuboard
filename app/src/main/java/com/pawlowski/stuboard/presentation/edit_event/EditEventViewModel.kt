package com.pawlowski.stuboard.presentation.edit_event

import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.presentation.filters.FilterModel
import com.pawlowski.stuboard.presentation.filters.FilterType
import com.pawlowski.stuboard.ui.event_editing.EditEventScreenType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class EditEventViewModel @Inject constructor(
    private val geocoder: Geocoder,
): IEditEventViewModel, ViewModel() {
    override val container: Container<EditEventUiState, EditEventSingleEvent> = container(
        EditEventUiState(categories = initialCategories())
    )

    private val positionUpdatesFlow = MutableSharedFlow<Unit>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun moveToNextPage() = intent {
        reduce {
            state.copy(currentPage = when(state.currentPage) {
                EditEventScreenType.FIRST -> { EditEventScreenType.SECOND }
                EditEventScreenType.SECOND -> { EditEventScreenType.THIRD }
                else -> {state.currentPage}
            })
        }
        emitNewPositionRefreshInQueue()
    }

    override fun moveToPreviousPage() = intent {
        reduce {
            state.copy(currentPage = when(state.currentPage) {
                EditEventScreenType.THIRD -> { EditEventScreenType.SECOND }
                EditEventScreenType.SECOND -> { EditEventScreenType.FIRST }
                else -> {state.currentPage}
            })
        }
        emitNewPositionRefreshInQueue()
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

    override fun changeCategorySelection(category: FilterModel, isSelected: Boolean) = intent {
        reduce {
            state.copy(categories = state.categories.toMutableMap().apply {
                val newMap = get(category.filterType)?.toMutableMap()?.apply {
                    set(category, isSelected)
                }
                set(category.filterType, newMap?: mapOf())
            })
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
        return mapOf(
            Pair(FilterType.CATEGORY, mapOf(
                Pair(FilterModel.Category("Koncerty", R.drawable.guitar_icon), false),
            )),
            Pair(FilterType.ACCESS, mapOf(
                Pair(FilterModel.Access.EVERYBODY, false),
                Pair(FilterModel.Access.PROTECTED, false)
            )),
            Pair(FilterType.REGISTRATION, mapOf(
                Pair(FilterModel.Registration.NoRegistrationNeeded, false),
                Pair(FilterModel.Registration.RegistrationNeeded, false)
            )),
            Pair(FilterType.ENTRY_PRICE, mapOf(
                Pair(FilterModel.EntryPrice.Free, false),
                Pair(FilterModel.EntryPrice.Paid,false)
            )),
            Pair(FilterType.OTHER, mapOf(
                Pair(FilterModel.Other.Outside, false),
                Pair(FilterModel.Other.Inside, false)
            ))

        )

    }

    init {
        handlePositionUpdates()
    }

}
