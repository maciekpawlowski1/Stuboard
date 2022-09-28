package com.pawlowski.stuboard.presentation.my_events

import androidx.lifecycle.ViewModel
import com.pawlowski.stuboard.data.mappers.toEventItemForPreview
import com.pawlowski.stuboard.data.mappers.toPublishingStatus
import com.pawlowski.stuboard.domain.models.Resource
import com.pawlowski.stuboard.presentation.use_cases.DeleteManyEditingEventsUseCase
import com.pawlowski.stuboard.presentation.use_cases.GetAllEditingEventsUseCase
import com.pawlowski.stuboard.presentation.use_cases.RefreshMyEventsUseCase
import com.pawlowski.stuboard.presentation.utils.UiText
import com.pawlowski.stuboard.ui.models.EventItemForPreview
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
class MyEventsViewModel @Inject constructor(
    private val getAllEditingEventsUseCase: GetAllEditingEventsUseCase,
    private val refreshMyEventsUseCase: RefreshMyEventsUseCase,
    private val deleteManyEditingEventsUseCase: DeleteManyEditingEventsUseCase,
): IMyEventsViewModel, ViewModel() {
    override val container: Container<MyEventsUiState, MyEventsSingleEvent> = container(MyEventsUiState.Loading)



    private fun observeMyEvents() = intent(registerIdling = false) {
        repeatOnSubscription {
            getAllEditingEventsUseCase().collectLatest {
                reduce {
                    MyEventsUiState.Success(it.associate {
                        Pair(
                            it.toEventItemForPreview(),
                            it.publishingStatus.toPublishingStatus()
                        )
                    })
                }
            }
        }

    }

    private fun refreshMyEvents() = intent {
        val result = refreshMyEventsUseCase()
        if(result is Resource.Error)
        {

        }
    }

    override fun changeSelectionOfItem(eventId: String) = intent {
        reduce {
            val stateValue = state
            if(stateValue is MyEventsUiState.Success)
            {
                stateValue.copy(selectedEvents = stateValue.selectedEvents.toMutableList().apply {
                    if(!contains(eventId))
                        add(eventId)
                    else
                        remove(eventId)
                }.toList())
            }
            else
                state
        }
    }

    override fun unselectAllEvents() = intent {
        reduce {
            val stateValue = state
            if(stateValue is MyEventsUiState.Success)
            {
                stateValue.copy(selectedEvents = listOf())
            }
            else
                state
        }
    }

    override fun onCardClick(item: Pair<EventItemForPreview, EventPublishState>) = intent {
        val currentState = state
        if(currentState is MyEventsUiState.Success && currentState.selectedEvents.isNotEmpty())
        {
            changeSelectionOfItem(item.first.eventId)
        }
        else
        {
            if(item.second == EventPublishState.EDITING)
                postSideEffect(MyEventsSingleEvent.NavigateToEditEvent(item.first.eventId))
            else
                postSideEffect(MyEventsSingleEvent.NavigateToEventPreview(item.first.eventId))
        }

    }

    override fun deleteSelectedEvents() = intent {
        val stateValue = state
        if (stateValue is MyEventsUiState.Success)
        {
            val selectedIds = stateValue.selectedEvents
            val events = stateValue.events
            val areAllInEditMode = selectedIds.all { id ->

                val eventState = events.entries.firstOrNull { it.key.eventId == id }?.value
                eventState?.let {
                   it == EventPublishState.EDITING
                }?:true
            }
            if(!areAllInEditMode)
            {
                postSideEffect(MyEventsSingleEvent.ShowErrorToast(UiText.StaticText("Wydarzenia które są już opublikowane nie zostały usunięte!")))
            }
            deleteManyEditingEventsUseCase(selectedIds.mapNotNull {
                try {
                    it.toInt()
                }
                catch (e: Exception) {
                    null
                }
            })
        }
    }

    override fun onBackClick() = intent {
        val currentState = state
        if(currentState is MyEventsUiState.Success && currentState.selectedEvents.isNotEmpty())
        {
            unselectAllEvents()
        }
        else
        {
            postSideEffect(MyEventsSingleEvent.NavigateBack)
        }
    }

    init {
        observeMyEvents()
        refreshMyEvents()
    }
}