package com.pawlowski.stuboard.presentation.edit_event

import com.pawlowski.stuboard.ui.event_editing.EditEventScreenType

data class EditEventUiState(
    val currentPage: EditEventScreenType = EditEventScreenType.FIRST,
    val tittleInput: String = ""
)
