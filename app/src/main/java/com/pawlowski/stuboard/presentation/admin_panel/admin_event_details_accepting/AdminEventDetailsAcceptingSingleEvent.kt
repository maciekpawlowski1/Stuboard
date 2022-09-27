package com.pawlowski.stuboard.presentation.admin_panel.admin_event_details_accepting

import com.pawlowski.stuboard.presentation.utils.UiText

sealed interface AdminEventDetailsAcceptingSingleEvent {
    data class ShowErrorToast(val uiText: UiText) : AdminEventDetailsAcceptingSingleEvent
    object NavigateBack: AdminEventDetailsAcceptingSingleEvent
}