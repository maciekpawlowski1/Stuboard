package com.pawlowski.stuboard.domain

import androidx.paging.PagingData
import com.pawlowski.stuboard.domain.models.Resource
import com.pawlowski.stuboard.presentation.event_details.EventDetailsResult
import com.pawlowski.stuboard.presentation.filters.FilterModel
import com.pawlowski.stuboard.presentation.home.HomeEventTypeSuggestion
import com.pawlowski.stuboard.ui.models.EventItemForMapScreen
import com.pawlowski.stuboard.ui.models.EventItemForPreview
import kotlinx.coroutines.flow.Flow

interface EventsRepository {
    fun getHomeEventTypesSuggestion(): Flow<List<HomeEventTypeSuggestion>>
    fun getEventDetails(eventId: Int): Flow<EventDetailsResult?>
    fun getEventResultStream(filters: List<FilterModel>): Flow<PagingData<EventItemForPreview>>
    suspend fun getEventsForMapScreen(filters: List<FilterModel>): Resource<List<EventItemForMapScreen>>
}