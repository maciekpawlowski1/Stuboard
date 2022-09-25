package com.pawlowski.stuboard.presentation.admin_panel.admin_event_accepting

import androidx.lifecycle.ViewModel
import com.pawlowski.stuboard.ui.utils.PreviewUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class AdminEventAcceptingViewModel @Inject constructor(

): IAdminEventAcceptingViewModel, ViewModel() {
    override val container: Container<AdminEventAcceptingUiState, AdminEventAcceptingSingleEvent> = container(
        AdminEventAcceptingUiState.Loading
    )

    private fun handleEventsLoad() = intent(registerIdling = false) {
        delay(2000)
        reduce {
            AdminEventAcceptingUiState.Success(PreviewUtils.defaultEventPreviews)
        }
    }

    init {
        handleEventsLoad()
    }
}