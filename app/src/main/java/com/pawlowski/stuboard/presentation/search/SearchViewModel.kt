package com.pawlowski.stuboard.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pawlowski.stuboard.presentation.use_cases.GetEventsPagingStreamUseCase
import com.pawlowski.stuboard.presentation.use_cases.GetSelectedFiltersUseCase
import com.pawlowski.stuboard.presentation.use_cases.UnselectAllFiltersUseCase
import com.pawlowski.stuboard.ui.models.EventItemForPreview
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSelectedFiltersUseCase: GetSelectedFiltersUseCase,
    private val getEventsPagingStreamUseCase: GetEventsPagingStreamUseCase,
    private val unselectAllFiltersUseCase: UnselectAllFiltersUseCase,
): ViewModel(), ISearchViewModel {
    private val initialUiState = SearchUiState(listOf())

    private val selectedFilters = getSelectedFiltersUseCase()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val pagingData: Flow<PagingData<EventItemForPreview>> = selectedFilters
        .distinctUntilChanged()
        .flatMapLatest {
            getEventsPagingStreamUseCase(it)
        }.cachedIn(viewModelScope)

    private val _lastSavedScrollPosition = MutableStateFlow(0)
    override val lastSavedScrollPosition: StateFlow<Int>
        get() = _lastSavedScrollPosition.asStateFlow()

    private val _uiState = MutableStateFlow(initialUiState)

    override val uiState: StateFlow<SearchUiState>
        get() = _uiState

    override fun onAction(action: SearchUiAction) {
        when (action)
        {
            is SearchUiAction.SaveScrollPosition -> {
                _lastSavedScrollPosition.update { action.scrollPosition }
            }
            is SearchUiAction.UnselectAllFilters -> {
                viewModelScope.launch {
                    unselectAllFiltersUseCase()
                }
            }
        }
    }

    val updateUiStateJob = selectedFilters
        .onEach{ selectedFilters ->
            _uiState.update {
                it.copy(selectedFilters = selectedFilters)
            }
        }
        .launchIn(viewModelScope)


}