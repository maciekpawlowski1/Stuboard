package com.pawlowski.stuboard.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawlowski.stuboard.presentation.use_cases.GetSelectedFiltersUseCase
import com.pawlowski.stuboard.ui.utils.PreviewUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getSelectedFiltersUseCase: GetSelectedFiltersUseCase,
): ViewModel(), IMapViewModel {
    private val initialUiState: MapUiState = MapUiState.Loading(listOf())
    private val _uiState = MutableStateFlow(initialUiState)

    private val selectedFilters = getSelectedFiltersUseCase()

    private val events = selectedFilters
        .flatMapLatest {
            flow {
                delay(20)
                emit(PreviewUtils.defaultEventItemsForMap) //TODO: Change to some repository invocation
            }
        }.onStart { emit(listOf()) }

    override val uiState: StateFlow<MapUiState>
        get() = _uiState.asStateFlow()

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