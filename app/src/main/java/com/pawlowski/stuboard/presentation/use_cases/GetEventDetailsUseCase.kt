package com.pawlowski.stuboard.presentation.use_cases

import com.pawlowski.stuboard.presentation.event_details.EventDetailsResult
import kotlinx.coroutines.flow.Flow

fun interface GetEventDetailsUseCase: (String) -> Flow<EventDetailsResult?>