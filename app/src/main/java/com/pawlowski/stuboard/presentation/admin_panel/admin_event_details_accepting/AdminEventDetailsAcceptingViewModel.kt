package com.pawlowski.stuboard.presentation.admin_panel.admin_event_details_accepting

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.pawlowski.stuboard.domain.models.Resource
import com.pawlowski.stuboard.presentation.use_cases.GetEventDetailsUseCase
import com.pawlowski.stuboard.presentation.use_cases.AcceptEventAsAdminUseCase
import com.pawlowski.stuboard.presentation.use_cases.CancelEventFromAdminPanelUseCase
import com.pawlowski.stuboard.presentation.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class AdminEventDetailsAcceptingViewModel @Inject constructor(
    private val getEventDetailsUseCase: GetEventDetailsUseCase,
    private val acceptEventAsAdminUseCase: AcceptEventAsAdminUseCase,
    private val cancelEventFromAdminPanelUseCase: CancelEventFromAdminPanelUseCase,
    savedStateHandle: SavedStateHandle,
): IAdminEventDetailsAcceptingViewModel, ViewModel() {
    private val eventId = savedStateHandle.get<String>("eventForAcceptId")!!

    override val container: Container<AdminEventDetailsAcceptingUiState, AdminEventDetailsAcceptingSingleEvent> = container(
        AdminEventDetailsAcceptingUiState.Loading
    )

    private fun handleEventLoad() = intent(registerIdling = false) {
        getEventDetailsUseCase(eventId).collect { result ->
            result?.let {
                reduce {
                    AdminEventDetailsAcceptingUiState.Success(
                        event = it.event,
                    )
                }
            }
        }
    }

    private fun handleRequest(request: suspend (String) -> Resource<Unit>) = intent {
        val currentStartState = state
        if(currentStartState is AdminEventDetailsAcceptingUiState.Success && !currentStartState.isRequestInProgress)
        {
            reduce {
                (state as AdminEventDetailsAcceptingUiState.Success).copy(isRequestInProgress = true)
            }
            val result = request(eventId)
            if(result is Resource.Success)
            {
                postSideEffect(AdminEventDetailsAcceptingSingleEvent.NavigateBack)
            }
            else {
                postSideEffect(AdminEventDetailsAcceptingSingleEvent.ShowErrorToast(result.message?:UiText.StaticText("Coś poszło nie tak")))
            }
            reduce {
                val currentState = state
                if(currentState is AdminEventDetailsAcceptingUiState.Success)
                {
                    currentState.copy(isRequestInProgress = false)
                }
                else
                    currentState
            }
        }
    }

    override fun acceptEvent() {
        handleRequest { return@handleRequest acceptEventAsAdminUseCase(eventId) }
    }

    override fun rejectEvent() {
        handleRequest { return@handleRequest cancelEventFromAdminPanelUseCase(eventId) }
    }

    init {
        handleEventLoad()
    }
}