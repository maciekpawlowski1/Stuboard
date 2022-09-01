package com.pawlowski.stuboard.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawlowski.stuboard.presentation.home.HomeUiAction.ClearAllFiltersAndSelectFilter
import com.pawlowski.stuboard.presentation.use_cases.GetHomeEventTypesSuggestionsUseCase
import com.pawlowski.stuboard.presentation.use_cases.GetPreferredCategoriesUseCase
import com.pawlowski.stuboard.presentation.use_cases.SelectNewFilterUseCase
import com.pawlowski.stuboard.presentation.use_cases.UnselectAllFiltersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPreferredCategoriesUseCase: GetPreferredCategoriesUseCase,
    private val getHomeEventTypesSuggestionsUseCase: GetHomeEventTypesSuggestionsUseCase,
    private val unselectAllFiltersUseCase: UnselectAllFiltersUseCase,
    private val selectNewFilterUseCase: SelectNewFilterUseCase,
): ViewModel(), IHomeViewModel {

    private val actionFlow = MutableSharedFlow<HomeUiAction>(extraBufferCapacity = 10)

    private val preferredCategories = getPreferredCategoriesUseCase()

    private val eventTypeSuggestion = getHomeEventTypesSuggestionsUseCase()
        .flowOn(Dispatchers.IO)

    override val uiState = combine(preferredCategories, eventTypeSuggestion)
    { categories, eventsSuggestionsState ->
        HomeUiState(eventsSuggestionsState, categories)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = HomeUiState(listOf(), listOf())
        )

    override fun onAction(homeUiAction: HomeUiAction) {
        viewModelScope.launch {
            actionFlow.emit(homeUiAction)
        }
    }

    private val actionHandlerJob = actionFlow.onEach {
        when(it)
        {
            is ClearAllFiltersAndSelectFilter -> {
                unselectAllFiltersUseCase()
                selectNewFilterUseCase(it.filterModel)
            }
        }
    }.launchIn(viewModelScope)
}