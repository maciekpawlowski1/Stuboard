package com.pawlowski.stuboard.presentation.event_status

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.pawlowski.stuboard.domain.EventsRepository
import com.pawlowski.stuboard.domain.models.Resource
import com.pawlowski.stuboard.presentation.my_events.EventPublishState
import com.pawlowski.stuboard.presentation.use_cases.GetEditingEventPreviewUseCase
import com.pawlowski.stuboard.presentation.use_cases.GetEventPublishingStatusUseCase
import com.pawlowski.stuboard.presentation.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class EventStatusViewModel @Inject constructor(
    private val getEventPublishingStatusUseCase: GetEventPublishingStatusUseCase,
    private val getEditingEventPreviewUseCase: GetEditingEventPreviewUseCase,
    private val eventsRepository: EventsRepository,
    private val savedStateHandle: SavedStateHandle,
): IEventStatusViewModel, ViewModel() {
    private val eventId: Int = savedStateHandle.get<String>("editEventId")?.toInt()!!

    override val container: Container<EventStatusUiState, EventStatusSingleEvent> = container(
        EventStatusUiState()
    )

    private fun handleEventPublishStatus() = intent(registerIdling = false) {
        repeatOnSubscription {
            getEventPublishingStatusUseCase(eventId).collectLatest {
                reduce {
                    state.copy(publishState = it)
                }
            }
        }
    }

    private fun handleEventItemForPreview() = intent(registerIdling = false) {
        repeatOnSubscription {
            getEditingEventPreviewUseCase(eventId).collectLatest {
                reduce {
                    state.copy(eventPreview = it)
                }
            }
        }
    }

    override fun publishEvent() = intent {
        if(!state.isRequestInProgress)
        {
            reduce {
                state.copy(isRequestInProgress = true)
            }
            val result = eventsRepository.publishEvent(eventId)
            reduce {
                state.copy(isRequestInProgress = false)
            }
            if(result is Resource.Error)
            {
                result.message?.let {
                    postSideEffect(EventStatusSingleEvent.ShowErrorToast(it))
                }
            }
        }
    }

    override fun onBackPressed() = intent {
        if(state.isRequestInProgress)
        {
            postSideEffect(EventStatusSingleEvent.ShowErrorToast(UiText.StaticText("Wait, request in progress!")))
        }
        else
        {
            if(state.publishState == EventPublishState.EDITING)
            {
                postSideEffect(EventStatusSingleEvent.NavigateBack)
            }
            else
            {
                postSideEffect(EventStatusSingleEvent.NavigateBackToMyEvents)
            }
        }
    }

    override fun cancelEvent() = intent {
        if(!state.isRequestInProgress)
        {
            reduce {
                state.copy(isRequestInProgress = true)
            }

            val result = eventsRepository.cancelEvent(eventId)

            if(result is Resource.Error)
            {
                postSideEffect(EventStatusSingleEvent.ShowErrorToast(result.message?:UiText.StaticText("Wystąpił błąd przy anulowaniu eventu")))
            }

            reduce {
                state.copy(isRequestInProgress = false)
            }
        }
    }

    init {
        handleEventPublishStatus()
        handleEventItemForPreview()
    }

}