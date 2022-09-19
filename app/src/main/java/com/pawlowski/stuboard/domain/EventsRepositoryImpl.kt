package com.pawlowski.stuboard.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pawlowski.stuboard.data.local.editing_events.EditingEventsDao
import com.pawlowski.stuboard.data.local.editing_events.FullEventEntity
import com.pawlowski.stuboard.data.mappers.toEditEventUiState
import com.pawlowski.stuboard.data.mappers.toEventItemForMapScreenList
import com.pawlowski.stuboard.data.mappers.toEventItemWithDetails
import com.pawlowski.stuboard.data.mappers.toFullEventEntity
import com.pawlowski.stuboard.data.remote.EventsService
import com.pawlowski.stuboard.domain.models.Resource
import com.pawlowski.stuboard.presentation.edit_event.EditEventUiState
import com.pawlowski.stuboard.presentation.event_details.EventDetailsResult
import com.pawlowski.stuboard.presentation.filters.FilterModel
import com.pawlowski.stuboard.presentation.home.HomeEventTypeSuggestion
import com.pawlowski.stuboard.presentation.my_events.EventPublishState
import com.pawlowski.stuboard.presentation.utils.UiText
import com.pawlowski.stuboard.ui.models.EventItemForMapScreen
import com.pawlowski.stuboard.ui.models.EventItemForPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EventsRepositoryImpl @Inject constructor(
    private val eventsService: EventsService,
    private val eventsDao: EditingEventsDao
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

    override fun getEventDetails(eventId: String): Flow<EventDetailsResult?> = flow {
        try {
           val response = eventsService.loadItemById(eventId)
            emit(EventDetailsResult(isFresh = true,
                event = response.body()!!.toEventItemWithDetails()))
        }
        catch (e: Exception) {

        }
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
        return try {
            val result = eventsService.loadItems(1, 100)
            if(result.code() == 200)
            {
                Resource.Success(result.body()!!.toEventItemForMapScreenList())
            }
            else
                Resource.Error(message = UiText.StaticText(result.message()))
        }
        catch (e: Exception)
        {
            Resource.Error(message = UiText.StaticText(e.localizedMessage?:"Error occurred"))
        }
    }

    override fun getEventPublishingStatus(eventId:Int): Flow<EventPublishState> = flow {
        val event = eventsDao.getEvent(eventId)
        val emitValue = when(event.publishingStatus) {
            0 -> EventPublishState.EDITING
            1 -> EventPublishState.WAITING_TO_PUBLISH
            2 -> EventPublishState.PUBLISHED
            3 -> EventPublishState.CANCELED
            else -> EventPublishState.EDITING
        }
        emit(emitValue)
    }

    override suspend fun saveEditingEvent(editEventUiState: EditEventUiState): Long {
        return eventsDao.upsertEvent(editEventUiState.toFullEventEntity())
    }

    override fun getAllEditingEvents(): Flow<List<FullEventEntity>> {
        return eventsDao.getAllEvents()
    }

    override suspend fun getEditingEventStateFromEditingEvent(eventId: Long): EditEventUiState {
       return eventsDao.getEvent(eventId.toInt()).toEditEventUiState()
    }
}