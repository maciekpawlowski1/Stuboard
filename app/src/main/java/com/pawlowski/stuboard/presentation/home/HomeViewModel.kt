package com.pawlowski.stuboard.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawlowski.stuboard.presentation.use_cases.GetHomeEventTypesSuggestionsUseCase
import com.pawlowski.stuboard.presentation.use_cases.GetPreferredCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPreferredCategoriesUseCase: GetPreferredCategoriesUseCase,
    private val getHomeEventTypesSuggestionsUseCase: GetHomeEventTypesSuggestionsUseCase
): ViewModel(), IHomeViewModel {

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
}