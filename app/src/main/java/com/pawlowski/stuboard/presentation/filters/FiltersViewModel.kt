package com.pawlowski.stuboard.presentation.filters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawlowski.stuboard.presentation.use_cases.GetAllSuggestedNotSelectedFiltersUseCase
import com.pawlowski.stuboard.presentation.use_cases.GetSelectedFiltersUseCase
import com.pawlowski.stuboard.presentation.use_cases.SelectNewFilterUseCase
import com.pawlowski.stuboard.presentation.use_cases.UnselectFilterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
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
    private val initialUiState: FiltersUiState = FiltersUiState(initialSearchTextValue, listOf(), mapOf())


    //Call emit only from onAction function, else be careful of suspending
    private val actionSharedFlow = MutableSharedFlow<FiltersScreenAction>(
        onBufferOverflow = BufferOverflow.SUSPEND,
        extraBufferCapacity = 0
    )

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

    private val _uiState = MutableStateFlow(initialUiState)
    override val uiState: StateFlow<FiltersUiState> get() = _uiState.asStateFlow()


    //Call it on every new uiAction, also inside this ViewModel to not care of suspending
    override fun onAction(action: FiltersScreenAction) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            actionSharedFlow.emit(action)
        }
    }


    private val uiStateUpdaterJob = //Update uiState when particular state change
        combine(searchText, selectedFilters, suggestedFilters)
        { searchText, selectedFilters, suggestedFilters ->
            val filteredSuggestions = if(searchText.isEmpty())
                suggestedFilters
            else
                suggestedFilters.filter { it.tittle.contains(searchText, ignoreCase = true) }
            val mappedSuggestions = filteredSuggestions
                .groupBy { it.filterType }
            _uiState.update {
                it.copy(
                    searchText= searchText,
                    selectedFilters = selectedFilters,
                    suggestedFilters = mappedSuggestions
                )
            }
        }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)


    private val uiActionHandlerJob = //Synchronous action handler
        actionSharedFlow
            .onEach {
                if(it is FiltersScreenAction.AddNewFilter)
                {
                    selectNewFilterUseCase(it.filterModel)
                    onAction(FiltersScreenAction.SearchTextChange(""))
                }
                else if(it is FiltersScreenAction.UnselectFilter)
                {
                    unselectFilterUseCase(it.filterModel)
                }

            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)

}