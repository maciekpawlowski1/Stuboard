package com.pawlowski.stuboard.ui.event_editing

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.presentation.my_events.*
import com.pawlowski.stuboard.ui.models.EventItemForPreview
import com.pawlowski.stuboard.ui.screens_in_bottom_navigation_related.screens.EventCard
import com.pawlowski.stuboard.ui.theme.Green
import com.pawlowski.stuboard.ui.theme.Orange
import com.pawlowski.stuboard.ui.theme.montserratFont
import com.pawlowski.stuboard.ui.utils.PreviewUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.annotation.OrbitInternal
import org.orbitmvi.orbit.syntax.ContainerContext

@Composable
fun MyEventsScreen(onNavigateBack: () -> Unit = {}, onNavigateToEventPreview: (eventId: String) -> Unit = {}, viewModel: IMyEventsViewModel = hiltViewModel<MyEventsViewModel>()) {
    val uiState = viewModel.container.stateFlow.collectAsState()

    val isLoadingState = derivedStateOf {
        uiState.value is MyEventsUiState.Loading
    }

    val eventsState = derivedStateOf {
        val uiStateValue = uiState.value
        if(uiStateValue is MyEventsUiState.Success)
            uiStateValue.events
        else
            mapOf()
    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart)
            {
                IconButton(onClick = { onNavigateBack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_back_icon),
                        contentDescription = "",
                    )
                }

            }
            Text(
                text = "Twoje wydarzenia:",
                fontFamily = montserratFont,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
            )
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd)
            {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.help_icon),
                        contentDescription = "",
                        tint = Green
                    )
                }
            }
        }

        LazyVerticalGrid(columns = GridCells.Fixed(2))
        {

            items(eventsState.value.toList()) {
                EventCardWithDotIndicator(event = it.first, indicatorColor = when(it.second) {
                    EventPublishState.EDITING -> { Orange }
                    EventPublishState.WAITING_TO_PUBLISH -> { Color.Yellow }
                    EventPublishState.PUBLISHED -> { Green }
                    EventPublishState.CANCELED -> { Color.Red }
                })
                {
                    onNavigateToEventPreview(it.first.eventId)
                }
            }
        }
    }
}

@Composable
fun EventCardWithDotIndicator(event: EventItemForPreview, indicatorColor: Color, onCardClick: () -> Unit)
{
    Box(modifier = Modifier
        .wrapContentSize()
    ) {
        EventCard(
            eventItemForPreview = event,
            modifier = Modifier
                .padding(vertical = 10.dp,horizontal = 6.dp)
        )
        {
            onCardClick()
        }

        Surface(
            modifier = Modifier
                .padding(top = 28.dp, end = 25.dp)
                .size(15.dp)
                .align(Alignment.TopEnd),
            shape = CircleShape,
            color = indicatorColor
        ) {}
    }
}

@OptIn(OrbitInternal::class)
@Preview(showBackground = true)
@Composable
fun MyEventsScreenPreview() {
    MyEventsScreen(viewModel = object : IMyEventsViewModel
    {

        override val container: Container<MyEventsUiState, MyEventsSingleEvent> =
            object : Container<MyEventsUiState, MyEventsSingleEvent>
            {
                override val settings: Container.Settings
                    get() = TODO("Not yet implemented")
                override val sideEffectFlow: Flow<MyEventsSingleEvent>
                    get() = TODO("Not yet implemented")
                override val stateFlow: StateFlow<MyEventsUiState> =
                    MutableStateFlow(MyEventsUiState.Success(
                        PreviewUtils.defaultEventPreviews.associateWith { EventPublishState.WAITING_TO_PUBLISH }
                    ))

                override suspend fun orbit(orbitIntent: suspend ContainerContext<MyEventsUiState, MyEventsSingleEvent>.() -> Unit) {
                    TODO("Not yet implemented")
                }

            }

    })
}