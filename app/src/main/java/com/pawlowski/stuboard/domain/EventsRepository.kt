package com.pawlowski.stuboard.domain

import com.pawlowski.stuboard.presentation.home.HomeEventTypeSuggestion
import kotlinx.coroutines.flow.Flow

interface EventsRepository {
    fun getHomeEventTypesSuggestion(): Flow<List<HomeEventTypeSuggestion>>
}