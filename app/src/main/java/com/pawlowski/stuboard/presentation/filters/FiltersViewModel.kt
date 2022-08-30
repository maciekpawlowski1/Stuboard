package com.pawlowski.stuboard.presentation.filters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawlowski.stuboard.presentation.use_cases.GetAllSuggestedNotSelectedFiltersUseCase
import com.pawlowski.stuboard.presentation.use_cases.GetSelectedFiltersUseCase
import com.pawlowski.stuboard.presentation.use_cases.SelectNewFilterUseCase
import com.pawlowski.stuboard.presentation.use_cases.UnselectFilterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FiltersViewModel @Inject constructor(
    private val getSelectedFiltersUseCase: GetSelectedFiltersUseCase,
    private val getAllSuggestedNotSelectedFiltersUseCase: GetAllSuggestedNotSelectedFiltersUseCase,
    private val selectNewFilterUseCase: SelectNewFilterUseCase,
    private val unselectFilterUseCase: UnselectFilterUseCase,
): ViewModel(), IFiltersViewModel {

    private val actionSharedFlow = MutableSharedFlow<FiltersScreenAction>()

    private val searchText = actionSharedFlow
        .filterIsInstance<FiltersScreenAction.SearchTextChange>()
        .distinctUntilChanged()
        .map { it.newText }
        .flowOn(Dispatchers.IO)
            //To keep replay value
        .shareIn(scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            replay = 1
        )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = ""
        )

    private val selectedFilters = getSelectedFiltersUseCase()
        .distinctUntilChanged()
        .onStart { listOf<FilterModel>() }


    private val suggestedFilters = getAllSuggestedNotSelectedFiltersUseCase()
        .distinctUntilChanged()
        .onStart { listOf<FilterModel>() }



    override val uiState: StateFlow<FiltersUiState> =
        combine(searchText, selectedFilters, suggestedFilters)
        { searchText, selectedFilters, suggestedFilters ->
            val mappedSuggestions = suggestedFilters.groupBy { it.filterType }
            FiltersUiState(searchText, selectedFilters, mappedSuggestions)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = FiltersUiState("", listOf(), mapOf())
        )

    override fun onAction(action: FiltersScreenAction) {
        viewModelScope.launch {
            actionSharedFlow.emit(action)

            when(action)
            {
                is FiltersScreenAction.AddNewFilter ->
                {
                    selectNewFilterUseCase.invoke(action.filterModel)
                    actionSharedFlow.emit(FiltersScreenAction.SearchTextChange(""))
                }
                is FiltersScreenAction.UnselectFilter ->
                {
                    unselectFilterUseCase.invoke(action.filterModel)
                }
                else -> {}
            }
        }
    }
}