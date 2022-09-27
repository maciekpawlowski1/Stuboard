package com.pawlowski.stuboard.presentation.admin_panel.admin_event_details_accepting

import org.orbitmvi.orbit.ContainerHost

interface IAdminEventDetailsAcceptingViewModel: ContainerHost<AdminEventDetailsAcceptingUiState, AdminEventDetailsAcceptingSingleEvent> {

    fun acceptEvent()
    fun rejectEvent()
}