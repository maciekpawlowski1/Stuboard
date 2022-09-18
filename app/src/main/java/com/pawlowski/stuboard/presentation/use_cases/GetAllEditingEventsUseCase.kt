package com.pawlowski.stuboard.presentation.use_cases

import com.pawlowski.stuboard.data.local.editing_events.FullEventEntity
import kotlinx.coroutines.flow.Flow

fun interface GetAllEditingEventsUseCase: () -> Flow<List<FullEventEntity>>