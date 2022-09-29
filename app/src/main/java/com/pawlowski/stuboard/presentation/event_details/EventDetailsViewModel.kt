package com.pawlowski.stuboard.presentation.event_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawlowski.stuboard.domain.models.Resource
import com.pawlowski.stuboard.presentation.use_cases.CancelEventFromAdminPanelUseCase
import com.pawlowski.stuboard.presentation.use_cases.GetEventDetailsUseCase
import com.pawlowski.stuboard.presentation.use_cases.GetIsUserAdminUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val getEventDetailsUseCase: GetEventDetailsUseCase,
    private val cancelEventFromAdminPanelUseCase: CancelEventFromAdminPanelUseCase,
    private val isUserAdminUseCase: GetIsUserAdminUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel(), IEventDetailsViewModel {

    private val eventId = savedStateHandle.get<String>("eventId")!!

    private val eventResult = getEventDetailsUseCase(eventId)
    private val isRefreshing = eventResult.map { it == null || !it.isFresh }
    private val _uiState = MutableStateFlow(EventDetailsUiState(isRefreshing = true, eventDetails = null))
    override val uiState get() = _uiState.asStateFlow()

    val updateUiStateJob = combine(eventResult, isRefreshing)
    { eventResult, isRefreshing ->
        Pair(first = isRefreshing, second = eventResult?.event)
    }.onEach { pair ->
        _uiState.update {
            it.copy(isRefreshing = pair.first, eventDetails = pair.second)
        }
    }.launchIn(viewModelScope)

    override fun deleteClick() {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            val event = _uiState.value.eventDetails
            _uiState.update {
                it.copy(isRefreshing = true, eventDetails = null)
            }
            val result = cancelEventFromAdminPanelUseCase(eventId)
            if(result is Resource.Success)
            {
                //TODO: Navigate back
            }
            else
            {
                //TODO: Show error toast
                _uiState.update {
                    it.copy(isRefreshing = false, eventDetails = event?:(it.eventDetails?.copy()))
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            val isAdmin = isUserAdminUseCase()
            if(isAdmin == true)
            {
                _uiState.update {
                    it.copy(showDeleteButton = true)
                }
            }
        }
    }
}