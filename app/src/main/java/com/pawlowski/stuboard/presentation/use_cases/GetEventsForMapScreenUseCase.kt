package com.pawlowski.stuboard.presentation.use_cases

import com.pawlowski.stuboard.domain.models.Resource
import com.pawlowski.stuboard.presentation.filters.FilterModel
import com.pawlowski.stuboard.ui.models.EventItemForMapScreen

fun interface GetEventsForMapScreenUseCase: suspend (List<FilterModel>) -> Resource<List<EventItemForMapScreen>>