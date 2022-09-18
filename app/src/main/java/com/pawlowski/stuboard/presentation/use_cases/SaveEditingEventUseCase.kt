package com.pawlowski.stuboard.presentation.use_cases

import com.pawlowski.stuboard.presentation.edit_event.EditEventUiState

fun interface SaveEditingEventUseCase: suspend (EditEventUiState) -> Long