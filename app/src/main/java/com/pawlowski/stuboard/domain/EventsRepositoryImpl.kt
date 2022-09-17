package com.pawlowski.stuboard.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pawlowski.stuboard.data.local.editing_events.FullEventEntity
import com.pawlowski.stuboard.data.remote.EventsService
import com.pawlowski.stuboard.domain.models.Resource
import com.pawlowski.stuboard.presentation.edit_event.EditEventUiState
import com.pawlowski.stuboard.presentation.event_details.EventDetailsResult
import com.pawlowski.stuboard.presentation.filters.FilterModel
import com.pawlowski.stuboard.presentation.home.HomeEventTypeSuggestion
import com.pawlowski.stuboard.presentation.my_events.EventPublishState
import com.pawlowski.stuboard.ui.models.EventItemForMapScreen
import com.pawlowski.stuboard.ui.models.EventItemForPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EventsRepositoryImpl @Inject constructor(
    private val eventsService: EventsService,
): EventsRepository {
    override fun getHomeEventTypesSuggestion(): Flow<List<HomeEventTypeSuggestion>> = flow {
        val firstEmit = listOf(
            HomeEventTypeSuggestion(
                suggestionType = "Najwcześniej",
                isLoading = true,
                events = listOf()
            ),
            HomeEventTypeSuggestion(
                suggestionType = "Online",
                isLoading = true,
                events = listOf(),
                suggestionFilters = listOf(FilterModel.Place.Online),
            )
        )
        emit(firstEmit)
    }

    override fun getEventDetails(eventId: Int): Flow<EventDetailsResult?> {
        TODO("Not yet implemented")
    }

    override fun getEventResultStream(filters: List<FilterModel>): Flow<PagingData<EventItemForPreview>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                EventsPagingSourceFactory(
                    eventsService = eventsService,
                    filters = filters
                )
            }
        ).flow
    }

    override suspend fun getEventsForMapScreen(filters: List<FilterModel>): Resource<List<EventItemForMapScreen>> {
        TODO("Not yet implemented")
    }

    override fun getEventPublishingStatus(): Flow<EventPublishState> {
        TODO("Not yet implemented")
    }

    override suspend fun saveEditingEvent(editEventUiState: EditEventUiState) {
        TODO("Not yet implemented")
    }

    override fun getAllEditingEvents(): Flow<List<FullEventEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun getEditingEventStateFromEditingEvent(eventId: Int): EditEventUiState {
        TODO("Not yet implemented")
    }
}