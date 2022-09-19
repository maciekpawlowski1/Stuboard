package com.pawlowski.stuboard.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pawlowski.stuboard.data.local.editing_events.FullEventEntity
import com.pawlowski.stuboard.data.remote.FakeEventsService
import com.pawlowski.stuboard.domain.models.Resource
import com.pawlowski.stuboard.presentation.edit_event.EditEventUiState
import com.pawlowski.stuboard.presentation.event_details.EventDetailsResult
import com.pawlowski.stuboard.presentation.filters.FilterModel
import com.pawlowski.stuboard.presentation.home.HomeEventTypeSuggestion
import com.pawlowski.stuboard.presentation.my_events.EventPublishState
import com.pawlowski.stuboard.ui.models.EventItemForMapScreen
import com.pawlowski.stuboard.ui.models.EventItemForPreview
import com.pawlowski.stuboard.ui.utils.PreviewUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeEventsRepositoryImpl(private val eventsService: FakeEventsService) : EventsRepository {
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
        delay(1500)
        val secondEmit = listOf(
            HomeEventTypeSuggestion(
                suggestionType = "Najwcześniej",
                isLoading = false,
                events = PreviewUtils.defaultEventPreviews
            ),
            HomeEventTypeSuggestion(
                suggestionType = "Online",
                isLoading = true,
                events = listOf(),
                suggestionFilters = listOf(FilterModel.Place.Online),
            )
        )
        emit(secondEmit)
        delay(1000)
        val thirdEmit = listOf(
            HomeEventTypeSuggestion(
                suggestionType = "Najwcześniej",
                isLoading = false,
                events = PreviewUtils.defaultEventPreviews
            ),
            HomeEventTypeSuggestion(suggestionType = "Online",
                isLoading = false,
                events = PreviewUtils.defaultEventPreviews.filter { it.place.lowercase() == "online" },
                suggestionFilters = listOf(FilterModel.Place.Online),
            )
        )
        emit(thirdEmit)
    }

    override fun getEventDetails(eventId: String): Flow<EventDetailsResult?> = flow {
        emit(null)
        delay(2000)
        emit(EventDetailsResult(event = PreviewUtils.defaultFullEvent, isFresh = true))
    }

    override fun getEventResultStream(filters: List<FilterModel>): Flow<PagingData<EventItemForPreview>> {
        return TODO()/*Pager(
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
        ).flow*/
    }

    override suspend fun getEventsForMapScreen(filters: List<FilterModel>): Resource<List<EventItemForMapScreen>> {
        delay(2120)
        return Resource.Success(PreviewUtils.defaultEventItemsForMap)
    }

    override fun getEventPublishingStatus(eventId: Int): Flow<EventPublishState> = flow {
        delay(2000)
        emit(EventPublishState.EDITING)
    }

    override suspend fun saveEditingEvent(editEventUiState: EditEventUiState): Long {
        TODO("Not yet implemented")
    }


    override fun getAllEditingEvents(): Flow<List<FullEventEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun getEditingEventStateFromEditingEvent(eventId: Long): EditEventUiState {
        TODO("Not yet implemented")
    }




}