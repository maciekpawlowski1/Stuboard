package com.pawlowski.stuboard.presentation.event_status

import com.pawlowski.stuboard.presentation.my_events.EventPublishState
import com.pawlowski.stuboard.ui.models.EventItemForPreview

data class EventStatusUiState(
    val eventPreview: EventItemForPreview? = null,
    val publishState: EventPublishState? = null
)
