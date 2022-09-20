package com.pawlowski.stuboard.presentation.use_cases

import com.pawlowski.stuboard.ui.models.EventItemForPreview
import kotlinx.coroutines.flow.Flow

fun interface GetEditingEventPreviewUseCase: (Int) -> Flow<EventItemForPreview>