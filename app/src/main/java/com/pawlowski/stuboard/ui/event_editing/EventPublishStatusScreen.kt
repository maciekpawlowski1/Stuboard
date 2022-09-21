package com.pawlowski.stuboard.ui.event_editing

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.presentation.event_status.EventStatusSingleEvent
import com.pawlowski.stuboard.presentation.event_status.EventStatusUiState
import com.pawlowski.stuboard.presentation.event_status.EventStatusViewModel
import com.pawlowski.stuboard.presentation.event_status.IEventStatusViewModel
import com.pawlowski.stuboard.presentation.my_events.EventPublishState
import com.pawlowski.stuboard.ui.models.EventItemForPreview
import com.pawlowski.stuboard.ui.screens_in_bottom_navigation_related.screens.EventCard
import com.pawlowski.stuboard.ui.theme.Green
import com.pawlowski.stuboard.ui.theme.LightGray
import com.pawlowski.stuboard.ui.theme.Orange
import com.pawlowski.stuboard.ui.theme.montserratFont
import com.pawlowski.stuboard.ui.utils.myLoadingEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.annotation.OrbitInternal
import org.orbitmvi.orbit.syntax.ContainerContext

@Composable
fun EventPublishStatusScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateBackToMyEvents: () -> Unit = {},
    viewModel: IEventStatusViewModel = hiltViewModel<EventStatusViewModel>()
)
{

    BackHandler {
        viewModel.onBackPressed()
    }

    val uiState = viewModel.container.stateFlow.collectAsState()
    val publishStatusState = derivedStateOf {
        uiState.value.publishState
    }
    val eventPreviewState = derivedStateOf {
        uiState.value.eventPreview
    }
    val isRequestInProgressState = derivedStateOf {
        uiState.value.isRequestInProgress
    }
    val context = LocalContext.current
    LaunchedEffect(true) {
        viewModel.container.sideEffectFlow.collect { event ->
            when(event) {
                is EventStatusSingleEvent.ShowErrorToast -> {
                    Toast.makeText(context, event.text.asString(context), Toast.LENGTH_LONG).show()
                }
                is EventStatusSingleEvent.NavigateBack -> {
                    onNavigateBack()
                }
                is EventStatusSingleEvent.NavigateBackToMyEvents -> {
                    onNavigateBackToMyEvents()
                }
            }
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(5.dp))
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart)
        {
            IconButton(onClick = { viewModel.onBackPressed() }) {
                Icon(painter = painterResource(id = R.drawable.arrow_back2_icon), contentDescription = "")
            }
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Status publikowania",
                fontFamily = montserratFont,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        val publishStateValue = publishStatusState.value
        StatusCard(
            modifier = Modifier
                .padding(horizontal = 15.dp),
            statusColor = when(publishStateValue) {
                EventPublishState.EDITING -> Orange
                EventPublishState.WAITING_TO_PUBLISH -> Color.Yellow
                EventPublishState.PUBLISHED -> Green
                EventPublishState.CANCELED -> Color.Red
                else -> LightGray
                                                  },
            statusText = when(publishStateValue) {
                EventPublishState.EDITING -> "Wydarzenie jest w trakcie edycji"
                EventPublishState.WAITING_TO_PUBLISH -> "Wydarzenie czeka na akceptację"
                EventPublishState.PUBLISHED -> "Wydarzenie jest opublikowane"
                EventPublishState.CANCELED -> "Wydarzenie zostało odrzucone. Wystąpił błąd? Napisz do nas!"
                else -> ""
            },
            showPublishButton = publishStateValue == EventPublishState.EDITING,
            isLoading = publishStateValue == null,
            onPublishClick = { viewModel.publishEvent() },
            isRequestInProgress = { isRequestInProgressState.value }
        )

        Spacer(modifier = Modifier.height(20.dp))
        if(isRequestInProgressState.value)
        {
            CircularProgressIndicator(color = Green, modifier = Modifier.size(40.dp))
        }
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "Podgląd wydarzenia:",
            fontFamily = montserratFont,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(15.dp))
        EventCard(eventItemForPreview = eventPreviewState.value?: EventItemForPreview(), isLoading = eventPreviewState.value == null)
    }
}

@Composable
fun StatusCard(modifier: Modifier = Modifier, statusColor: Color, statusText: String, showPublishButton: Boolean, isLoading: Boolean = false, onPublishClick: () -> Unit = {}, isRequestInProgress: () -> Boolean = {false})
{
    Card(modifier = modifier
        .fillMaxWidth()
        .height(105.dp), shape = RectangleShape, elevation = 7.dp) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center)
        {
            Row(modifier = Modifier.fillMaxWidth(0.9f), verticalAlignment = Alignment.CenterVertically) {
                Card(shape = CircleShape, modifier = Modifier
                    .size(50.dp)
                    .myLoadingEffect(isLoading), backgroundColor = statusColor) {

                }
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    modifier= Modifier
                        .padding(end = 10.dp)
                        .fillMaxWidth()
                        .myLoadingEffect(isLoading),
                    text = statusText,
                    fontFamily = montserratFont,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp
                )
            }

            if(!isLoading && showPublishButton && !isRequestInProgress())
            {
                TextButton(modifier = Modifier.align(Alignment.BottomEnd),onClick = { onPublishClick() }) {
                    Text(text = "Opublikuj", color = Green)
                }
            }
        }
    }
}

@OptIn(OrbitInternal::class)
@Preview(showBackground = true)
@Composable
fun EventPublishStatusScreenPreview()
{
    EventPublishStatusScreen(viewModel = object : IEventStatusViewModel {
        override fun publishEvent() {}
        override fun onBackPressed() {}

        override val container: Container<EventStatusUiState, EventStatusSingleEvent> = object : Container<EventStatusUiState, EventStatusSingleEvent> {
            override val settings: Container.Settings
                get() = TODO("Not yet implemented")
            override val sideEffectFlow: Flow<EventStatusSingleEvent>
                get() = TODO("Not yet implemented")
            override val stateFlow: StateFlow<EventStatusUiState> = MutableStateFlow(
                EventStatusUiState(publishState = EventPublishState.EDITING)
            )

            override suspend fun orbit(orbitIntent: suspend ContainerContext<EventStatusUiState, EventStatusSingleEvent>.() -> Unit) {
                TODO("Not yet implemented")
            }

        }

    })
}