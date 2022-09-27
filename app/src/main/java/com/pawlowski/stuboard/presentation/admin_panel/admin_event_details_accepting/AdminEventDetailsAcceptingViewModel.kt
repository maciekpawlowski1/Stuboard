package com.pawlowski.stuboard.presentation.admin_panel.admin_event_details_accepting

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.pawlowski.stuboard.domain.models.Resource
import com.pawlowski.stuboard.presentation.use_cases.GetEventDetailsUseCase
import com.pawlowski.stuboard.presentation.use_cases.AcceptEventAsAdminUseCase
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
    savedStateHandle: SavedStateHandle,
): IAdminEventDetailsAcceptingViewModel, ViewModel() {
    private val eventId = savedStateHandle.get<String>("eventForAcceptId")!!

    override val container: Container<AdminEventDetailsAcceptingUiState, AdminEventDetailsAcceptingSingleEvent> = container(
        AdminEventDetailsAcceptingUiState.Loading
    )

    private fun handleEventLoad() = intent(registerIdling = false) {
        println("Loading event: $eventId")

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

    override fun acceptEvent() = intent {
        if(state is AdminEventDetailsAcceptingUiState.Success)
        {
            reduce {
                (state as AdminEventDetailsAcceptingUiState.Success).copy(isRequestInProgress = true)
            }
            val result = acceptEventAsAdminUseCase(eventId)
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

    override fun rejectEvent() = intent {

    }

    init {
        handleEventLoad()
    }
}