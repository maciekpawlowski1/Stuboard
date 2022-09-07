package com.pawlowski.stuboard.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawlowski.stuboard.presentation.filters.FilterModel
import com.pawlowski.stuboard.presentation.use_cases.GetSelectedFiltersUseCase
import com.pawlowski.stuboard.ui.utils.PreviewUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getSelectedFiltersUseCase: GetSelectedFiltersUseCase,
): ViewModel(), IMapViewModel {
    private val initialUiState: MapUiState = MapUiState.Loading(listOf())
    private val _uiState = MutableStateFlow(initialUiState)

    override val uiState: StateFlow<MapUiState>
        get() = _uiState.asStateFlow()

    private val defaultPlaceFilter = FilterModel.Place.RealPlace("Kraków") //TODO: Change to choosing from preferences

    private val selectedFilters = getSelectedFiltersUseCase()
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

    @OptIn(ExperimentalCoroutinesApi::class)
    private val events = selectedFilters
        .flatMapLatest {
            flow {
                delay(2120)
                emit(PreviewUtils.defaultEventItemsForMap) //TODO: Change to some repository invocation
            }
        }.onStart { emit(listOf()) }



    private val updateUiStateJob = combine(selectedFilters, events)
    { currentFilters, events ->
        _uiState.update {
            if(events.isEmpty())
            {
                MapUiState.Loading(_currentFilters = currentFilters)
            }
            else
            {
                MapUiState.Success(_currentFilters = currentFilters, events = events)
            }
        }
    }.launchIn(viewModelScope)
}