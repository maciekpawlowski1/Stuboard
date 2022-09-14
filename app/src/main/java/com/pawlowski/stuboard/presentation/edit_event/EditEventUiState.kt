package com.pawlowski.stuboard.presentation.edit_event

import com.google.android.gms.maps.model.LatLng
import com.pawlowski.stuboard.presentation.filters.FilterModel
import com.pawlowski.stuboard.presentation.filters.FilterType
import com.pawlowski.stuboard.ui.event_editing.EditEventScreenType

data class EditEventUiState(
    val currentPage: EditEventScreenType = EditEventScreenType.FIRST,
    val tittleInput: String = "",
    val sinceTime: Long? = null,
    val toTime: Long? = null,
    val categories: Map<FilterType, Map<FilterModel, Boolean>> = mapOf(),
    val isOnline: Boolean = true,
    val city: String = "",
    val streetAndNumber: String = "",
    val country: String = "",
    val placeName: String = "",
    val positionOnMap: LatLng? = null,
    val markerDrawableRes: Int? = null,
    val organisationSearchText: String = "",
    val selectedOrganisation: Organisation? = null,
    val suggestedOrganisations: List<Organisation.Existing> = listOf(),
)
