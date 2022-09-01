package com.pawlowski.stuboard.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.pawlowski.stuboard.presentation.use_cases.GetEventsPagingStreamUseCase
import com.pawlowski.stuboard.presentation.use_cases.GetSelectedFiltersUseCase
import com.pawlowski.stuboard.ui.models.EventItemForPreview
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSelectedFiltersUseCase: GetSelectedFiltersUseCase,
    private val getEventsPagingStreamUseCase: GetEventsPagingStreamUseCase,
): ViewModel(), ISearchViewModel {
    private val initialUiState = SearchUiState(listOf())

    private val selectedFilters = getSelectedFiltersUseCase()

    override val pagingData: Flow<PagingData<EventItemForPreview>> = getEventsPagingStreamUseCase(
        listOf())

    private val _uiState = MutableStateFlow(initialUiState)
    override val uiState: StateFlow<SearchUiState>
        get() = _uiState

    val updateUiStateJob = selectedFilters
        .onEach{ selectedFilters ->
            _uiState.update {
                it.copy(selectedFilters = selectedFilters)
            }
        }
        .launchIn(viewModelScope)


}