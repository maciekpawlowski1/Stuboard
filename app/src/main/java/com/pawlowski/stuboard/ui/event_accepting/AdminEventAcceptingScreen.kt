package com.pawlowski.stuboard.ui.event_accepting

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pawlowski.stuboard.presentation.admin_panel.admin_event_accepting.AdminEventAcceptingSingleEvent
import com.pawlowski.stuboard.presentation.admin_panel.admin_event_accepting.AdminEventAcceptingUiState
import com.pawlowski.stuboard.presentation.admin_panel.admin_event_accepting.AdminEventAcceptingViewModel
import com.pawlowski.stuboard.presentation.admin_panel.admin_event_accepting.IAdminEventAcceptingViewModel
import com.pawlowski.stuboard.ui.models.EventItemForPreview
import com.pawlowski.stuboard.ui.screens_in_bottom_navigation_related.screens.EventCard
import com.pawlowski.stuboard.ui.theme.montserratFont
import com.pawlowski.stuboard.ui.utils.PreviewUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.annotation.OrbitInternal
import org.orbitmvi.orbit.syntax.ContainerContext

@Composable
fun AdminEventAcceptingScreen(viewModel: IAdminEventAcceptingViewModel = hiltViewModel<AdminEventAcceptingViewModel>()) {
    val uiState = viewModel.container.stateFlow.collectAsState()
    val isLoadingState = derivedStateOf {
        uiState.value is AdminEventAcceptingUiState.Loading
    }
    val eventsState = derivedStateOf {
        val uiStateValue = uiState.value
        if(uiStateValue is AdminEventAcceptingUiState.Success)
        {
            uiStateValue.events
        }
        else
        {
            listOf()
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            modifier = Modifier.align(CenterHorizontally),
            text = "Wydarzenia do akceptacji:",
            fontSize = 13.sp,
            fontFamily = montserratFont,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyVerticalGrid(
            state = rememberLazyGridState(),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(
                start = 10.dp,
                end = 10.dp,
                bottom = 80.dp,
            )
        )
        {
            if(isLoadingState.value)
            {
                items(4)
                {
                    EventCard(
                        modifier = Modifier.padding(
                            vertical = 10.dp,
                            horizontal = 6.dp
                        ), eventItemForPreview = PreviewUtils.defaultEventPreviews[0],
                        isLoading = true
                    ) {
                        //TODO: Navigate
                    }
                }
            }
            items(eventsState.value)
            {
                EventCard(
                    modifier = Modifier.padding(
                        vertical = 10.dp,
                        horizontal = 6.dp
                    ), eventItemForPreview = it
                ) {
                    //TODO: Navigate
                }
            }
        }
    }
}

@OptIn(OrbitInternal::class)
@Preview(showBackground = true)
@Composable
fun AdminEventAcceptingScreenPreview()
{
    AdminEventAcceptingScreen(viewModel = object : IAdminEventAcceptingViewModel
    {
        override val container: Container<AdminEventAcceptingUiState, AdminEventAcceptingSingleEvent> =
            object : Container<AdminEventAcceptingUiState, AdminEventAcceptingSingleEvent>
        {
            override val settings: Container.Settings
                get() = TODO("Not yet implemented")
            override val sideEffectFlow: Flow<AdminEventAcceptingSingleEvent>
                get() = TODO("Not yet implemented")
            override val stateFlow: StateFlow<AdminEventAcceptingUiState> = MutableStateFlow(
                AdminEventAcceptingUiState.Success(
                    PreviewUtils.defaultEventPreviews
                )
            )

            override suspend fun orbit(orbitIntent: suspend ContainerContext<AdminEventAcceptingUiState, AdminEventAcceptingSingleEvent>.() -> Unit) {
                TODO("Not yet implemented")
            }

        }

    })
}