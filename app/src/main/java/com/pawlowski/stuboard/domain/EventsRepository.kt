package com.pawlowski.stuboard.domain

import com.pawlowski.stuboard.presentation.event_details.EventDetailsResult
import com.pawlowski.stuboard.presentation.home.HomeEventTypeSuggestion
import kotlinx.coroutines.flow.Flow

interface EventsRepository {
    fun getHomeEventTypesSuggestion(): Flow<List<HomeEventTypeSuggestion>>
    fun getEventDetails(eventId: Int): Flow<EventDetailsResult?>
}