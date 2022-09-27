package com.pawlowski.stuboard.ui.event_accepting

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.presentation.admin_panel.admin_event_details_accepting.AdminEventDetailsAcceptingSingleEvent
import com.pawlowski.stuboard.presentation.admin_panel.admin_event_details_accepting.AdminEventDetailsAcceptingUiState
import com.pawlowski.stuboard.presentation.admin_panel.admin_event_details_accepting.AdminEventDetailsAcceptingViewModel
import com.pawlowski.stuboard.presentation.admin_panel.admin_event_details_accepting.IAdminEventDetailsAcceptingViewModel
import com.pawlowski.stuboard.presentation.event_details.EventDetailsUiState
import com.pawlowski.stuboard.presentation.event_details.IEventDetailsViewModel
import com.pawlowski.stuboard.ui.other_screens.EventDetailsScreen
import com.pawlowski.stuboard.ui.theme.Green
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun AdminEventDetailsAcceptingScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: IAdminEventDetailsAcceptingViewModel = hiltViewModel<AdminEventDetailsAcceptingViewModel>()
) {

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect { event ->
            when(event) {
                is AdminEventDetailsAcceptingSingleEvent.NavigateBack -> {
                    onNavigateBack()
                }
                is AdminEventDetailsAcceptingSingleEvent.ShowErrorToast -> {
                    Toast.makeText(context, event.uiText.asString(context), Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    val uiState = viewModel.container.stateFlow.collectAsState()

    val isLoadingState = derivedStateOf {
        uiState.value is AdminEventDetailsAcceptingUiState.Loading
    }

    val isRequestInProgressState = derivedStateOf {
        val uiStateValue =uiState.value
        if(uiStateValue is AdminEventDetailsAcceptingUiState.Success)
            uiStateValue.isRequestInProgress
        else
            false

    }

    val eventState = derivedStateOf {
        val uiStateValue = uiState.value
        if(uiStateValue is AdminEventDetailsAcceptingUiState.Success)
        {
            uiStateValue.event
        }
        else
            null
    }
    val screenState = remember {
        MutableStateFlow(EventDetailsUiState(
            eventDetails = eventState.value,
            isRefreshing = isLoadingState.value)
        )
    }

    LaunchedEffect(eventState.value, isLoadingState.value) {
        screenState.value = EventDetailsUiState(isRefreshing = isLoadingState.value,
        eventDetails = eventState.value)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        EventDetailsScreen(viewModel = object : IEventDetailsViewModel {
            override val uiState: StateFlow<EventDetailsUiState> = screenState
        })

        ControlPanel(
            modifier = Modifier.align(Alignment.BottomCenter),
            onAcceptClick = {
                viewModel.acceptEvent()
            },
            onRejectClick = {
                viewModel.rejectEvent()
            },
            buttonsEnabled = {
                !isRequestInProgressState.value
            }
        )
    }
}

@Composable
private fun ControlPanel(
    modifier: Modifier = Modifier,
    onAcceptClick: () -> Unit = {},
    onRejectClick: () -> Unit = {},
    buttonsEnabled: () -> Boolean = {true},
)
{
    Card(modifier = modifier
        .height(70.dp)
        .fillMaxWidth(),
    shape = RectangleShape) {
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Button(modifier = Modifier.size(52.dp), enabled = buttonsEnabled(), onClick = { onRejectClick() }, shape = CircleShape, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)) {
                Icon(painter = painterResource(id = R.drawable.close_icon), contentDescription = "", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(30.dp))
            Button(modifier = Modifier.size(52.dp), enabled = buttonsEnabled(), onClick = { onAcceptClick() }, shape = CircleShape, colors = ButtonDefaults.buttonColors(backgroundColor = Green)) {
                Icon(painter = painterResource(id = R.drawable.ok_icon), contentDescription = "", tint = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminEventDetailsAcceptingScreenPreview() {
    AdminEventDetailsAcceptingScreen()
}