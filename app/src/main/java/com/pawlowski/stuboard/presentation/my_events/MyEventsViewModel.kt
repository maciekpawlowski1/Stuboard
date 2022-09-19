package com.pawlowski.stuboard.presentation.my_events

import androidx.lifecycle.ViewModel
import com.pawlowski.stuboard.data.mappers.isFree
import com.pawlowski.stuboard.presentation.use_cases.GetAllEditingEventsUseCase
import com.pawlowski.stuboard.ui.models.EventItemForPreview
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MyEventsViewModel @Inject constructor(
    private val getAllEditingEventsUseCase: GetAllEditingEventsUseCase,
): IMyEventsViewModel, ViewModel() {
    override val container: Container<MyEventsUiState, MyEventsSingleEvent> = container(MyEventsUiState.Loading)



    private fun observeMyEvents() = intent(registerIdling = false) {
        getAllEditingEventsUseCase().collectLatest {
            reduce {
                MyEventsUiState.Success(it.associate {
                    Pair(
                        EventItemForPreview(
                            eventId = it.id.toString(),
                            tittle = it.tittle,
                            place = if(it.city.isNotEmpty() && it.streetAndNumber.isNotEmpty())
                                "${it.city}, ${it.streetAndNumber}"
                            else
                                "",
                            isFree = it.isFree()
                        ),
                        EventPublishState.EDITING
                    )
                })
            }
        }
    }

    init {
        observeMyEvents()
    }
}