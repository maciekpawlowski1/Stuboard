package com.pawlowski.stuboard.presentation.admin_panel.admin_event_accepting

import com.pawlowski.stuboard.ui.models.EventItemForPreview

sealed class AdminEventAcceptingUiState
{
    object Loading: AdminEventAcceptingUiState()
    data class Success(val events: List<EventItemForPreview>): AdminEventAcceptingUiState()
}
