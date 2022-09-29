package com.pawlowski.stuboard.ui.event_editing

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
fun MyEventsScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToEditEvent: (eventId: String) -> Unit = {},
    onNavigateToEventPreview: (eventId: String) -> Unit = {},
    viewModel: IMyEventsViewModel = hiltViewModel<MyEventsViewModel>(),
) {
    BackHandler {
        viewModel.onBackClick()
    }

    val uiState = viewModel.container.stateFlow.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect { event ->
            when(event) {
                is MyEventsSingleEvent.NavigateToEventPreview -> {
                    onNavigateToEventPreview(event.eventId)
                }
                is MyEventsSingleEvent.NavigateToEditEvent -> {
                    onNavigateToEditEvent(event.eventId)
                }
                is MyEventsSingleEvent.ShowErrorToast -> {
                    Toast.makeText(context, event.text.asString(context), Toast.LENGTH_LONG).show()
                }
                is MyEventsSingleEvent.NavigateBack -> {
                    onNavigateBack()
                }
            }
        }
    }

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

    val selectedEventsState = derivedStateOf {
        val uiStateValue = uiState.value
        if(uiStateValue is MyEventsUiState.Success)
        {
            uiStateValue.selectedEvents
        }
        else
            listOf()
    }

    val isSomethingSelectedState = derivedStateOf {
        selectedEventsState.value.isNotEmpty()
    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart)
            {
                IconButton(onClick = { viewModel.onBackClick() }) {
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
                Row {
                    if(isSomethingSelectedState.value && !isLoadingState.value)
                    {
                        IconButton(onClick = { viewModel.deleteSelectedEvents() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.delete_icon),
                                contentDescription = "",
                                tint = Color.Red
                            )
                        }
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.help_icon),
                            contentDescription = "",
                            tint = Green
                        )
                    }
                }
            }
        }

        LazyVerticalGrid(columns = GridCells.Fixed(2))
        {
            item(span = { GridItemSpan(2) }) {
                if(isLoadingState.value)
                {
                    CircularProgressIndicator(
                        color = Green,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
            items(eventsState.value.toList()) {
                val selectedIds = selectedEventsState.value
                val isSelected = remember(selectedIds) {
                    selectedIds.contains(it.first.eventId)
                }
                EventCardWithDotIndicator(event = it.first, indicatorColor = when(it.second) {
                    EventPublishState.EDITING -> { Orange }
                    EventPublishState.WAITING_TO_PUBLISH -> { Color.Yellow }
                    EventPublishState.PUBLISHED -> { Green }
                    EventPublishState.CANCELED -> { Color.Red }
                },
                isSelected = {
                     isSelected
                },
                onCardLongClick = {
                    viewModel.changeSelectionOfItem(it.first.eventId)
                })
                {
                    viewModel.onCardClick(it)
                }
            }
        }
    }
}

@Composable
fun EventCardWithDotIndicator(
    event: EventItemForPreview,
    indicatorColor: Color,
    onCardLongClick: () -> Unit = {},
    isSelected: () -> Boolean,
    onCardClick: () -> Unit,
)
{
    Box(modifier = Modifier
        .wrapContentSize()
    ) {
        EventCard(
            eventItemForPreview = event,
            modifier = Modifier
                .padding(vertical = 10.dp,horizontal = 6.dp),
            onCardLongClick = {
                onCardLongClick()
            },
            showAsSelected = { isSelected() }
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
        override fun changeSelectionOfItem(eventId: String) {
            TODO("Not yet implemented")
        }

        override fun onCardClick(item: Pair<EventItemForPreview, EventPublishState>) {
            TODO("Not yet implemented")
        }

        override fun deleteSelectedEvents() {
            TODO("Not yet implemented")
        }

        override fun onBackClick() {
            TODO("Not yet implemented")
        }

        override fun unselectAllEvents() {
            TODO("Not yet implemented")
        }


        override val container: Container<MyEventsUiState, MyEventsSingleEvent> =
            object : Container<MyEventsUiState, MyEventsSingleEvent>
            {
                override val settings: Container.Settings
                    get() = TODO("Not yet implemented")
                override val sideEffectFlow: Flow<MyEventsSingleEvent>
                    get() = TODO("Not yet implemented")
                override val stateFlow: StateFlow<MyEventsUiState> =
                    MutableStateFlow(
                        MyEventsUiState.Success(
                            PreviewUtils.defaultEventPreviews.associateWith { EventPublishState.WAITING_TO_PUBLISH }
                        )
                    )

                override suspend fun orbit(orbitIntent: suspend ContainerContext<MyEventsUiState, MyEventsSingleEvent>.() -> Unit) {
                    TODO("Not yet implemented")
                }

            }

    })
}