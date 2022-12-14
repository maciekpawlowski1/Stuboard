package com.pawlowski.stuboard.ui.event_editing

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pawlowski.stuboard.presentation.edit_event.*
import com.pawlowski.stuboard.presentation.filters.FilterModel
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
fun EditEventScreen(
    viewModel: IEditEventViewModel = hiltViewModel<EditEventViewModel>(),
    onNavigateToEventPublishingScreen: (String) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {

    BackHandler {
        viewModel.moveToPreviousPage()
    }

    val context = LocalContext.current
    LaunchedEffect(true) {
        viewModel.container.sideEffectFlow.collect { event ->
            when(event) {
                is EditEventSingleEvent.NavigateToPublishing -> {
                    onNavigateToEventPublishingScreen(event.eventId.toString())
                }
                is EditEventSingleEvent.NavigateBack -> {
                    onNavigateBack()
                }
                is EditEventSingleEvent.ShowErrorToast -> {
                    Toast.makeText(context, event.text.asString(context), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

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

    val imageUrlState = derivedStateOf {
        uiState.value.imageUrl
    }

    val categoriesState = derivedStateOf {
        uiState.value.categories
    }

    val isOnlineState = derivedStateOf {
        uiState.value.isOnline
    }

    val cityState = derivedStateOf {
        uiState.value.city
    }

    val streetState = derivedStateOf {
        uiState.value.streetAndNumber
    }

    val countryState = derivedStateOf {
        uiState.value.country
    }

    val placeNameState = derivedStateOf {
        uiState.value.placeName
    }

    val positionOnMapState = derivedStateOf {
        uiState.value.positionOnMap
    }

    val markerResState = derivedStateOf {
        uiState.value.markerDrawableRes
    }

    val organisationSearchInputState = derivedStateOf {
        uiState.value.organisationSearchText
    }

    val suggestedOrganisationsState = derivedStateOf {
        uiState.value.suggestedOrganisations
    }

    val selectedOrganisationState = derivedStateOf {
        uiState.value.selectedOrganisation
    }

    val descriptionState = derivedStateOf {
        uiState.value.description
    }

    val websiteState = derivedStateOf {
        uiState.value.site
    }

    val facebookSiteState = derivedStateOf {
        uiState.value.facebookSite
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
                        onToTimeChange = { viewModel.changeToTime(it) },
                        onImageUriChange = { viewModel.changeImageUri(it) },
                        imageUrl = { imageUrlState.value?:"" }
                    )
                }
                EditEventScreenType.SECOND -> {
                    EditEventScreen2(categories = {
                        categoriesState.value
                    }, onCategorySelectionChange = { category, isSelected ->
                        viewModel.changeCategorySelection(category, isSelected)
                    },
                    onIsOnlineSelectedChange = {
                        viewModel.changeIsOnline(it)
                    },
                    isOnlineSelected = {
                        isOnlineState.value
                    })
                }
                EditEventScreenType.THIRD -> {
                    EditEventScreen3(
                        city = { cityState.value },
                        streetAndNum = { streetState.value },
                        country = { countryState.value },
                        placeName = { placeNameState.value },
                        onCityChange = { viewModel.changeCityInput(it) },
                        onCountryChange = { viewModel.changeCountryInput(it) },
                        onPlaceNameChange = { viewModel.changePlaceNameInput(it) },
                        onStreetChange = { viewModel.changeStreetInput(it) },
                        positionOnMap = { positionOnMapState.value },
                        markerRes = { markerResState.value }
                    )
                }
                EditEventScreenType.FOURTH -> {
                    EditEventScreen4(
                        organisationSearchInput = { organisationSearchInputState.value },
                        onOrganisationSearchInputChange = { viewModel.changeOrganisationSearchInput(it) },
                        suggestedOrganisations = { suggestedOrganisationsState.value },
                        selectedOrganisation = { selectedOrganisationState.value },
                        onOrganisationSelected = { viewModel.changeSelectedOrganisation(it) }
                    )
                }
                EditEventScreenType.FIFTH -> {
                    EditEventScreen5(
                        descriptionInput = { descriptionState.value },
                        facebookSiteInput = { facebookSiteState.value},
                        siteInput = { websiteState.value },
                        onDescriptionInputChange = { viewModel.changeDescriptionInput(it) },
                        onSiteInputChange = { viewModel.changeSiteInput(it) },
                        onFacebookSiteInputChange = { viewModel.changeFacebookSiteInput(it) },
                        onMoveToPublishingClick = { viewModel.validateAndMoveToPublishing() }
                    )
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
                val text =
                    if(currentScreenNum != allScreensCount)
                        "Przejd?? dalej"
                else
                    "Zobacz podgl??d"
                Text(text = text, color = Green)
            }

        }
    }
}

enum class EditEventScreenType(val num: Int) {
    FIRST(1),
    SECOND(2),
    THIRD(3),
    FOURTH(4),
    FIFTH(5),
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
        override fun changeCategorySelection(category: FilterModel, isSelected: Boolean) {}
        override fun changeIsOnline(isOnline: Boolean) {}
        override fun changeCountryInput(newValue: String) {}
        override fun changeStreetInput(newValue: String) {}
        override fun changeCityInput(newValue: String) {}
        override fun changePlaceNameInput(newValue: String) {}
        override fun changeOrganisationSearchInput(newValue: String) {}
        override fun changeSelectedOrganisation(organisation: Organisation) {}
        override fun changeDescriptionInput(newValue: String) {}
        override fun changeSiteInput(newValue: String) {}
        override fun changeFacebookSiteInput(newValue: String) {}
        override fun validateAndMoveToPublishing() {}
        override fun changeImageUri(imageUri: String) {}

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