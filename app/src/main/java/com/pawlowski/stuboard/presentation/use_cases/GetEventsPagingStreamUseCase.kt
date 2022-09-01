package com.pawlowski.stuboard.presentation.use_cases

import androidx.paging.PagingData
import com.pawlowski.stuboard.presentation.filters.FilterModel
import com.pawlowski.stuboard.ui.models.EventItemForPreview
import kotlinx.coroutines.flow.Flow

fun interface GetEventsPagingStreamUseCase: (List<FilterModel>) -> Flow<PagingData<EventItemForPreview>>