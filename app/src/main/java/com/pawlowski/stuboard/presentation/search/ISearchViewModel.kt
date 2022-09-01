package com.pawlowski.stuboard.presentation.search

import androidx.paging.PagingData
import com.pawlowski.stuboard.ui.models.EventItemForPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ISearchViewModel {
    val uiState: StateFlow<SearchUiState>
    val pagingData: Flow<PagingData<EventItemForPreview>>
}