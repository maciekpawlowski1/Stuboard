package com.pawlowski.stuboard.presentation.admin_panel.admin_event_details_accepting

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.pawlowski.stuboard.presentation.admin_panel.admin_event_accepting.AdminEventAcceptingSingleEvent
import com.pawlowski.stuboard.presentation.use_cases.GetEventDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class AdminEventDetailsAcceptingViewModel @Inject constructor(
    private val getEventDetailsUseCase: GetEventDetailsUseCase,
    savedStateHandle: SavedStateHandle,
): IAdminEventDetailsAcceptingViewModel, ViewModel() {
    private val eventId = savedStateHandle.get<String>("eventForAcceptId")

    override val container: Container<AdminEventDetailsAcceptingUiState, AdminEventAcceptingSingleEvent> = container(
        AdminEventDetailsAcceptingUiState.Loading
    )

    private fun handleEventLoad() = intent(registerIdling = false) {
        println("Loading event: $eventId")
        eventId?.let { id ->
            getEventDetailsUseCase(id).collect { result ->
                result?.let {
                    reduce {
                        AdminEventDetailsAcceptingUiState.Success(
                            event = it.event,
                        )
                    }
                }
            }

        }
    }

    init {
        handleEventLoad()
    }
}