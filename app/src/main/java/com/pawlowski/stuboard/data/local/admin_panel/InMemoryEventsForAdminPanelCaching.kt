package com.pawlowski.stuboard.data.local.admin_panel

import com.pawlowski.stuboard.data.remote.models.EventsResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InMemoryEventsForAdminPanelCaching @Inject constructor() {
    private val _eventsState: MutableStateFlow<EventsResponse?> = MutableStateFlow(null)
    val eventsState get() = _eventsState.map { it?.toList() }

    fun updateEvents(newEvents: EventsResponse)
    {
        _eventsState.value = newEvents
    }

    fun deleteEventFromCache(eventId: String)
    {
        _eventsState.update {
            it?.let {
                it.removeIf { event -> event.id == eventId }
                it
            }
        }
    }

    fun areEventsInCache(): Boolean
    {
        return _eventsState.value != null
    }


}