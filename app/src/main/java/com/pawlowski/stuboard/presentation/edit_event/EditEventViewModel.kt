package com.pawlowski.stuboard.presentation.edit_event

import androidx.lifecycle.ViewModel
import com.pawlowski.stuboard.ui.event_editing.EditEventScreenType
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class EditEventViewModel @Inject constructor(

): IEditEventViewModel, ViewModel() {
    override fun moveToNextPage() = intent {
        reduce {
            state.copy(currentPage = when(state.currentPage) {
                EditEventScreenType.FIRST -> { EditEventScreenType.SECOND }
                EditEventScreenType.SECOND -> { EditEventScreenType.THIRD }
                else -> {state.currentPage}
            })
        }
    }

    override fun moveToPreviousPage() = intent {
        reduce {
            state.copy(currentPage = when(state.currentPage) {
                EditEventScreenType.THIRD -> { EditEventScreenType.SECOND }
                EditEventScreenType.SECOND -> { EditEventScreenType.FIRST }
                else -> {state.currentPage}
            })
        }
    }

    override val container: Container<EditEventUiState, EditEventSingleEvent> = container(
        EditEventUiState()
    )

}