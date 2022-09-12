package com.pawlowski.stuboard.presentation.my_events

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MyEventsViewModel @Inject constructor(

): IMyEventsViewModel, ViewModel() {
    override val container: Container<MyEventsUiState, MyEventsSingleEvent> = container(MyEventsUiState.Loading)



    private fun observeMyEvents() = intent(registerIdling = false) {
        //TODO:
    }

    init {
        observeMyEvents()
    }
}