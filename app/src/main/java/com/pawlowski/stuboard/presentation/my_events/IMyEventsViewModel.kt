package com.pawlowski.stuboard.presentation.my_events

import com.pawlowski.stuboard.ui.models.EventItemForPreview
import org.orbitmvi.orbit.ContainerHost

interface IMyEventsViewModel: ContainerHost<MyEventsUiState, MyEventsSingleEvent> {
    fun changeSelectionOfItem(eventId: String)
    fun onCardClick(item: Pair<EventItemForPreview, EventPublishState>)
    fun deleteSelectedEvents()
    fun onBackClick()
    fun unselectAllEvents()
}