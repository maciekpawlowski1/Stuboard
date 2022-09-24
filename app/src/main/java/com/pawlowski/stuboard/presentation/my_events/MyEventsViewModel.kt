package com.pawlowski.stuboard.presentation.my_events

import androidx.lifecycle.ViewModel
import com.pawlowski.stuboard.data.mappers.isFree
import com.pawlowski.stuboard.data.mappers.toEventItemForPreview
import com.pawlowski.stuboard.data.mappers.toPublishingStatus
import com.pawlowski.stuboard.domain.models.Resource
import com.pawlowski.stuboard.presentation.use_cases.GetAllEditingEventsUseCase
import com.pawlowski.stuboard.presentation.use_cases.RefreshMyEventsUseCase
import com.pawlowski.stuboard.ui.models.EventItemForPreview
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MyEventsViewModel @Inject constructor(
    private val getAllEditingEventsUseCase: GetAllEditingEventsUseCase,
    private val refreshMyEventsUseCase: RefreshMyEventsUseCase,
): IMyEventsViewModel, ViewModel() {
    override val container: Container<MyEventsUiState, MyEventsSingleEvent> = container(MyEventsUiState.Loading)



    private fun observeMyEvents() = intent(registerIdling = false) {
        repeatOnSubscription {
            getAllEditingEventsUseCase().collectLatest {
                it.forEach { println(it.toString()) }
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

    init {
        observeMyEvents()
        refreshMyEvents()
    }
}