package com.pawlowski.stuboard.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pawlowski.stuboard.data.EventsPagingSourceFactory
import com.pawlowski.stuboard.data.FakeEventsService
import com.pawlowski.stuboard.presentation.event_details.EventDetailsResult
import com.pawlowski.stuboard.presentation.filters.FilterModel
import com.pawlowski.stuboard.presentation.home.HomeEventTypeSuggestion
import com.pawlowski.stuboard.ui.models.EventItemForPreview
import com.pawlowski.stuboard.ui.utils.PreviewUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeEventsRepositoryImpl(private val eventsService: FakeEventsService): EventsRepository {
    override fun getHomeEventTypesSuggestion(): Flow<List<HomeEventTypeSuggestion>> = flow {
        val firstEmit = listOf(
            HomeEventTypeSuggestion(suggestionType = "Najwcześniej", isLoading = true, listOf()),
            HomeEventTypeSuggestion(suggestionType = "Online", isLoading = true, listOf())
        )
        emit(firstEmit)
        delay(1500)
        val secondEmit = listOf(
        HomeEventTypeSuggestion(suggestionType = "Najwcześniej", isLoading = false, PreviewUtils.defaultEventPreviews),
        HomeEventTypeSuggestion(suggestionType = "Online", isLoading = true, listOf())
        )
        emit(secondEmit)
        delay(1000)
        val thirdEmit = listOf(
            HomeEventTypeSuggestion(suggestionType = "Najwcześniej", isLoading = false, PreviewUtils.defaultEventPreviews),
            HomeEventTypeSuggestion(suggestionType = "Online", isLoading = false, PreviewUtils.defaultEventPreviews.filter { it.place.lowercase() == "online" })
        )
        emit(thirdEmit)
    }

    override fun getEventDetails(eventId: Int): Flow<EventDetailsResult?> = flow {
        emit(null)
        delay(2000)
        emit(EventDetailsResult(event = PreviewUtils.defaultFullEvent, isFresh = true))
    }

    override fun getEventResultStream(filters: List<FilterModel>): Flow<PagingData<EventItemForPreview>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { EventsPagingSourceFactory(eventsService = eventsService, filters = filters)}
        ).flow
    }


}