package com.pawlowski.stuboard.presentation.map

import androidx.lifecycle.ViewModel
import com.pawlowski.stuboard.domain.models.Resource
import com.pawlowski.stuboard.presentation.filters.FilterModel
import com.pawlowski.stuboard.presentation.use_cases.GetEventsForMapScreenUseCase
import com.pawlowski.stuboard.presentation.use_cases.GetSelectedFiltersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
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
        repeatOnSubscription {
            getSelectedFiltersUseCase()
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
                .collect {
                    reduce {
                        MapUiState.Loading(it)
                    }
                    fetchEventsForFilters(it)
                }
        }
    }

    private fun fetchEventsForFilters(filters: List<FilterModel>) = intent {
        when(val result = getEventsForMapScreenUseCase(filters)) {
            is Resource.Success -> {
                reduce {
                    result.data?.let {
                        MapUiState.Success(events = it, _currentFilters = state.currentFilters)
                    }?: kotlin.run {
                        state
                    }
                }
            }
            else -> {
                //TODO: Show error state
            }
        }
    }

    init {
        observeSelectedFilters()
    }
}