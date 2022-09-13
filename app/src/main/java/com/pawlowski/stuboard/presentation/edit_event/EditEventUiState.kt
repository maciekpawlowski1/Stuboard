package com.pawlowski.stuboard.presentation.edit_event

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
)
