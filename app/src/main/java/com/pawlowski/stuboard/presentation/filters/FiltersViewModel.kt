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
    private val initialSearchTextValue = ""

    private val actionSharedFlow = MutableSharedFlow<FiltersScreenAction>()

    private val searchText = actionSharedFlow
        .filterIsInstance<FiltersScreenAction.SearchTextChange>()
        .distinctUntilChanged()
        .map { it.newText }
        .onStart { emit(initialSearchTextValue) }

    private val selectedFilters = getSelectedFiltersUseCase()
        .distinctUntilChanged()
        .onStart { emit(listOf()) }


    private val suggestedFilters = getAllSuggestedNotSelectedFiltersUseCase()
        .distinctUntilChanged()
        .onStart { emit(listOf()) }



    override val uiState: StateFlow<FiltersUiState> =
        combine(searchText, selectedFilters, suggestedFilters)
        { searchText, selectedFilters, suggestedFilters ->
            val filteredSuggestions = if(searchText.isEmpty())
                suggestedFilters
            else
                suggestedFilters.filter { it.tittle.contains(searchText, ignoreCase = true) }
            val mappedSuggestions = filteredSuggestions
                .groupBy { it.filterType }
            FiltersUiState(searchText, selectedFilters, mappedSuggestions)
        }
            //.flowOn(Dispatchers.IO) //if it's here,
            // problem starts with TextField onValueChange (sometimes resets the current string to "")
            // - problem showed only on my real device, not emulator.
            // Maybe try to separate the searchText state from uiState and try again with Dispatchers.IO here?
            // TODO: Check it
            .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = FiltersUiState(initialSearchTextValue, listOf(), mapOf())
        )

    override fun onAction(action: FiltersScreenAction) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            actionSharedFlow.emit(action)
            if(action is FiltersScreenAction.SearchTextChange)
                println("TextChange: ${action.newText}")
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