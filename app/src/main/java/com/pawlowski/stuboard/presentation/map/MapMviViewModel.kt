package com.pawlowski.stuboard.presentation.map

import androidx.lifecycle.ViewModel
import com.pawlowski.stuboard.domain.models.Resource
import com.pawlowski.stuboard.presentation.filters.FilterModel
import com.pawlowski.stuboard.presentation.use_cases.GetEventsForMapScreenUseCase
import com.pawlowski.stuboard.presentation.use_cases.GetSelectedFiltersUseCase
import com.pawlowski.stuboard.ui.models.EventItemForMapScreen
import com.pawlowski.stuboard.ui.models.EventMarker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MapMviViewModel @Inject constructor(
    private val getSelectedFiltersUseCase: GetSelectedFiltersUseCase,
    private val getEventsForMapScreenUseCase: GetEventsForMapScreenUseCase
): IMapMviViewModel, ViewModel() {
    override val container: Container<MapUiState, MapSingleEvent> = container(MapUiState.Loading(
        listOf()))

    private val defaultPlaceFilter = FilterModel.Place.RealPlace("Kraków") //TODO: Change to choosing from preferences


    private fun observeSelectedFilters() = intent(registerIdling = false) {
        val filtersFlow = getSelectedFiltersUseCase()
            .distinctUntilChanged()
            .map { //If there's no place filters, than add default one
                val filters = if(it.filterIsInstance<FilterModel.Place.RealPlace>().isEmpty())
                {
                    it.toMutableList().
                    apply {
                        add(0, defaultPlaceFilter)
                        removeAll(it.filterIsInstance<FilterModel.Place.Online>()) //Remove online filters because they won't be on map
                    }
                }
                else
                    it

                return@map filters.sortedByDescending { filter -> filter is FilterModel.Place.RealPlace }
            }
            .flowOn(Dispatchers.IO)

        filtersFlow.collectLatest {
            reduce {
                MapUiState.Loading(it)
            }
            when(val result = getEventsForMapScreenUseCase(it)) {
                is Resource.Success -> {
                    reduce {
                        result.data?.let { data ->
                            MapUiState.Success(events = data,
                                _currentFilters = state.currentFilters,
                                markers = calculateMarkersFromEvents(
                                    events = data,
                                    selectedEventId = data.getOrNull(0)?.eventId?: kotlin.run { -1 }
                                )
                            )
                        }?: kotlin.run {
                            state
                        }
                    }
                    postSideEffect(MapSingleEvent.AnimatedScrollToPage(0))
                    result.data?.getOrNull(0)?.position?.let {
                        postSideEffect(MapSingleEvent.AnimatedMoveMapToPosition(it))
                    }
                }
                else -> {
                    //TODO: Show error state
                }
            }
        }


    }

    private fun calculateMarkersFromEvents(events: List<EventItemForMapScreen>, selectedEventId: Int): List<EventMarker> {
        return events.map {
            EventMarker(
                position = it.position,
                iconId = if (it.eventId == selectedEventId)
                    it.mainCategoryDrawableIdWhenSelected
                else
                    it.mainCategoryDrawableId,
                eventTittle = it.tittle,
                eventId = it.eventId
            )
        }
    }

    override fun onPageChanged(index: Int) = intent {
        reduce {
            when(val currentState = state) {
                is MapUiState.Success -> {
                    val currentEvents = currentState.events
                    val newEventId = currentEvents.getOrNull(index)?.eventId?: kotlin.run { -1 }
                    currentState.copy(selectedEventId = newEventId, markers = calculateMarkersFromEvents(currentEvents, newEventId))
                }
                else -> {currentState}
            }
        }
        state.let { currentState ->
            if(currentState is MapUiState.Success)
            {
                currentState.events
                    .firstOrNull {
                        it.eventId == currentState.selectedEventId
                    }?.position?.let { position ->
                        postSideEffect(MapSingleEvent.AnimatedMoveMapToPosition(position))
                    }

            }
        }
    }

    override fun onEventSelected(eventId: Int) = intent {
        reduce {
            when(val currentState = state) {
                is MapUiState.Success -> {
                    currentState.copy(selectedEventId = eventId, markers = calculateMarkersFromEvents(currentState.events, eventId))
                }
                else -> {currentState}
            }
        }
        state.let { currentState ->
            if(currentState is MapUiState.Success)
            {
                val index = currentState.events.indexOfLast { it.eventId == eventId }
                if(index != -1)
                {
                    postSideEffect(MapSingleEvent.AnimatedScrollToPage(index))
                }
            }
        }
    }

    init {
        observeSelectedFilters()
    }
}