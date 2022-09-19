package com.pawlowski.stuboard.presentation.event_status

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.pawlowski.stuboard.presentation.use_cases.GetEventPublishingStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class EventStatusViewModel @Inject constructor(
    private val getEventPublishingStatusUseCase: GetEventPublishingStatusUseCase,
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

    init {
        handleEventPublishStatus()
    }

}