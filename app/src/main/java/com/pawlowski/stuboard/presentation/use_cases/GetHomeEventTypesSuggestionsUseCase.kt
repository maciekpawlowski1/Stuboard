package com.pawlowski.stuboard.presentation.use_cases

import com.pawlowski.stuboard.presentation.home.HomeEventTypeSuggestion
import kotlinx.coroutines.flow.Flow

fun interface GetHomeEventTypesSuggestionsUseCase: () -> Flow<List<HomeEventTypeSuggestion>>