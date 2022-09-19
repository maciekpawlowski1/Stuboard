package com.pawlowski.stuboard.presentation.use_cases

import com.pawlowski.stuboard.presentation.my_events.EventPublishState
import kotlinx.coroutines.flow.Flow

fun interface GetEventPublishingStatusUseCase: (Int) -> Flow<EventPublishState>