package com.pawlowski.stuboard.ui.event_editing

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pawlowski.stuboard.presentation.edit_event.EditEventSingleEvent
import com.pawlowski.stuboard.presentation.edit_event.EditEventUiState
import com.pawlowski.stuboard.presentation.edit_event.EditEventViewModel
import com.pawlowski.stuboard.presentation.edit_event.IEditEventViewModel
import com.pawlowski.stuboard.ui.register_screen.swappingTransitionSpec
import com.pawlowski.stuboard.ui.theme.Green
import com.pawlowski.stuboard.ui.theme.montserratFont
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.annotation.OrbitInternal
import org.orbitmvi.orbit.syntax.ContainerContext


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun EditEventScreen(viewModel: IEditEventViewModel = hiltViewModel<EditEventViewModel>()) {
    val uiState = viewModel.container.stateFlow.collectAsState()
    val currentScreenState = derivedStateOf {
        uiState.value.currentPage
    }
    val tittleInputState = derivedStateOf {
        uiState.value.tittleInput
    }

    val sinceTimeState = derivedStateOf {
        uiState.value.sinceTime
    }

    val toTimeState = derivedStateOf {
        uiState.value.toTime
    }

    Column {
        AnimatedContent(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            targetState = currentScreenState.value,
            transitionSpec = {
                swappingTransitionSpec()
                { prev, new ->
                    prev.num < new.num
                }
            }
        ) { screen ->
            when (screen) {
                EditEventScreenType.FIRST -> {
                    EditEventScreen1(
                        tittleInput = { tittleInputState.value },
                        onTittleInputChange = { viewModel.changeTittleInput(it) },
                        sinceTime = sinceTimeState.value,
                        toTime = toTimeState.value,
                        onSinceTimeChange = { viewModel.changeSinceTime(it) },
                        onToTimeChange = { viewModel.changeToTime(it) }
                    )
                }
                EditEventScreenType.SECOND -> {
                    EditEventScreen2()
                }
                else -> {

                }
            }
        }

        NavigationBox(currentScreenState.value.num, 5, onNextClick = {
            viewModel.moveToNextPage()
        }, onPreviousClick = {
            viewModel.moveToPreviousPage()
        })
    }

}

@Composable
private fun NavigationBox(
    currentScreenNum: Int,
    allScreensCount: Int,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        elevation = 7.dp
    )
    {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center)
        {
            if (currentScreenNum != 1) {
                TextButton(
                    modifier = Modifier
                        .align(Alignment.CenterStart),
                    onClick = { onPreviousClick() },
                ) {
                    Text(text = "Poprzednia", color = Green)
                }
            }

            Text(
                text = "$currentScreenNum z $allScreensCount",
                fontFamily = montserratFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
            )
            TextButton(
                modifier = Modifier
                    .align(Alignment.CenterEnd),
                onClick = { onNextClick() },
            ) {
                Text(text = "Przejdź dalej", color = Green)
            }

        }
    }
}

enum class EditEventScreenType(val num: Int) {
    FIRST(1),
    SECOND(2),
    THIRD(3)
}

@OrbitInternal
@Preview(showBackground = true)
@Composable
private fun EditEventScreenPreview() {
    EditEventScreen(viewModel = object : IEditEventViewModel {
        override fun moveToNextPage() {}
        override fun moveToPreviousPage() {}
        override fun changeTittleInput(newValue: String) {}
        override fun changeSinceTime(newTime: Long) {}
        override fun changeToTime(newTime: Long) {}
        override val container: Container<EditEventUiState, EditEventSingleEvent> =
            object : Container<EditEventUiState, EditEventSingleEvent> {
                override val stateFlow: StateFlow<EditEventUiState> =
                    MutableStateFlow(EditEventUiState())

                override val settings: Container.Settings
                    get() = TODO("Not yet implemented")
                override val sideEffectFlow: Flow<EditEventSingleEvent>
                    get() = TODO("Not yet implemented")

                override suspend fun orbit(orbitIntent: suspend ContainerContext<EditEventUiState, EditEventSingleEvent>.() -> Unit) {}
            }

    })
}