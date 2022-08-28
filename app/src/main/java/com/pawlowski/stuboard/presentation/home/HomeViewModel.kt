package com.pawlowski.stuboard.presentation.home

import androidx.lifecycle.ViewModel
import com.pawlowski.stuboard.presentation.use_cases.GetHomeEventTypesSuggestionsUseCase
import com.pawlowski.stuboard.presentation.use_cases.GetPreferredCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPreferredCategoriesUseCase: GetPreferredCategoriesUseCase,
    private val getHomeEventTypesSuggestionsUseCase: GetHomeEventTypesSuggestionsUseCase
): ViewModel() {

    private val preferredCategories = getPreferredCategoriesUseCase()

    private val eventTypeSuggestion = getHomeEventTypesSuggestionsUseCase()
        .flowOn(Dispatchers.IO)

    val uiState = combine(preferredCategories, eventTypeSuggestion)
    { categories, eventsSuggestionsState ->
        HomeUiState(eventsSuggestionsState, categories)
    }
}