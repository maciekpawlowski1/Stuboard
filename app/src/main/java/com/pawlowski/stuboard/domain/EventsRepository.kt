package com.pawlowski.stuboard.domain

import androidx.paging.PagingData
import com.pawlowski.stuboard.data.local.editing_events.FullEventEntity
import com.pawlowski.stuboard.domain.models.Resource
import com.pawlowski.stuboard.presentation.edit_event.EditEventUiState
import com.pawlowski.stuboard.presentation.event_details.EventDetailsResult
import com.pawlowski.stuboard.presentation.filters.FilterModel
import com.pawlowski.stuboard.presentation.home.HomeEventTypeSuggestion
import com.pawlowski.stuboard.presentation.my_events.EventPublishState
import com.pawlowski.stuboard.ui.models.EventItemForPreviewWithLocation
import com.pawlowski.stuboard.ui.models.EventItemForPreview
import kotlinx.coroutines.flow.Flow

interface EventsRepository {
    fun getHomeEventTypesSuggestion(): Flow<List<HomeEventTypeSuggestion>>
    fun getEventDetails(eventId: String): Flow<EventDetailsResult?>
    fun getEventResultStream(filters: List<FilterModel>): Flow<PagingData<EventItemForPreview>>
    suspend fun getEventsForMapScreen(filters: List<FilterModel>): Resource<List<EventItemForPreviewWithLocation>>
    fun getEventPublishingStatus(eventId: Int): Flow<EventPublishState>
    suspend fun saveEditingEvent(editEventUiState: EditEventUiState): Long
    fun getAllEditingEvents(): Flow<List<FullEventEntity>>
    suspend fun getEditingEventStateFromEditingEvent(eventId: Long): EditEventUiState
    fun getEditingEventPreview(eventId: Int): Flow<EventItemForPreview>
    suspend fun publishMyEvent(eventId: Int): Resource<Unit>
    suspend fun cancelMyEvent(eventId: Int): Resource<Unit>
    suspend fun refreshMyEvents(): Resource<Unit>
    suspend fun getEventsForAdminPanel(): Flow<List<EventItemForPreview>>
    suspend fun publishEventFromAdminPanel(eventId: String): Resource<Unit>
    suspend fun cancelEventFromAdminPanel(eventId: String): Resource<Unit>
    suspend fun deleteEditingEvents(eventsIds: List<Int>)
}