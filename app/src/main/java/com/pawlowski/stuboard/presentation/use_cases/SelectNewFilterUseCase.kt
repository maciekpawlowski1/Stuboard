package com.pawlowski.stuboard.presentation.use_cases

import com.pawlowski.stuboard.presentation.filters.FilterModel

fun interface SelectNewFilterUseCase: suspend (FilterModel) -> Unit