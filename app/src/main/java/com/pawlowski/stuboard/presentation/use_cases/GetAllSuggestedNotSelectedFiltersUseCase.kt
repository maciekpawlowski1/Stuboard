package com.pawlowski.stuboard.presentation.use_cases

import com.pawlowski.stuboard.presentation.filters.FilterModel
import kotlinx.coroutines.flow.Flow

fun interface GetAllSuggestedNotSelectedFiltersUseCase: () -> Flow<List<FilterModel>>