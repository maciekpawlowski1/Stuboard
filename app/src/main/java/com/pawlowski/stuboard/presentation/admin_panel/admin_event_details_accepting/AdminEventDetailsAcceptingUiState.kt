package com.pawlowski.stuboard.presentation.admin_panel.admin_event_details_accepting

import com.pawlowski.stuboard.ui.models.EventItemWithDetails

sealed class AdminEventDetailsAcceptingUiState
{
    object Loading: AdminEventDetailsAcceptingUiState()
    data class Success(val event: EventItemWithDetails): AdminEventDetailsAcceptingUiState()
}
