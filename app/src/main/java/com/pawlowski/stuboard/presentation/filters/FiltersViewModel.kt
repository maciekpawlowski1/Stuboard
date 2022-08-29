package com.pawlowski.stuboard.presentation.filters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawlowski.stuboard.presentation.use_cases.GetSelectedFiltersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FiltersViewModel @Inject constructor(
    private val getSelectedFiltersUseCase: GetSelectedFiltersUseCase,
): ViewModel(), IFiltersViewModel {

    private val actionSharedFlow = MutableSharedFlow<FiltersScreenAction>()

    private val searchText = actionSharedFlow
        .onSubscription { FiltersScreenAction.SearchTextChange("") }
        .filterIsInstance<FiltersScreenAction.SearchTextChange>()
        .map { it.newText }
        .distinctUntilChanged()


    private val selectedFilters = getSelectedFiltersUseCase()
        .onStart { listOf<FilterModel>() }
        .distinctUntilChanged()

    override val uiState: StateFlow<FiltersUiState> =
        combine(searchText, selectedFilters)
        { searchText, selectedFilters ->
            FiltersUiState(searchText, selectedFilters)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = FiltersUiState("", listOf())
        )

    override fun onAction(action: FiltersScreenAction) {
        viewModelScope.launch {
            actionSharedFlow.emit(action)
        }
    }
}