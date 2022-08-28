package com.pawlowski.stuboard.domain

import com.pawlowski.stuboard.presentation.home.HomeEventTypeSuggestion
import com.pawlowski.stuboard.ui.utils.PreviewUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeEventsRepositoryImpl: EventsRepository {
    override fun getHomeEventTypesSuggestion(): Flow<List<HomeEventTypeSuggestion>> = flow {
        val firstEmit = listOf(
            HomeEventTypeSuggestion(suggestionType = "Najwcześniej", isLoading = true, listOf()),
            HomeEventTypeSuggestion(suggestionType = "Online", isLoading = true, listOf())
        )
        emit(firstEmit)
        delay(500)
        val secondEmit = listOf(
        HomeEventTypeSuggestion(suggestionType = "Najwcześniej", isLoading = true, PreviewUtils.defaultEventPreviews),
        HomeEventTypeSuggestion(suggestionType = "Online", isLoading = true, listOf())
        )
        emit(secondEmit)
        delay(300)
        val thirdEmit = listOf(
            HomeEventTypeSuggestion(suggestionType = "Najwcześniej", isLoading = true, PreviewUtils.defaultEventPreviews),
            HomeEventTypeSuggestion(suggestionType = "Online", isLoading = true, PreviewUtils.defaultEventPreviews.filter { it.place.lowercase() == "online" })
        )
        emit(thirdEmit)
    }
}