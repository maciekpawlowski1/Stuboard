package com.pawlowski.stuboard.ui.screens_in_bottom_navigation_related.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.presentation.filters.FilterModel
import com.pawlowski.stuboard.presentation.home.HomeUiAction
import com.pawlowski.stuboard.presentation.home.HomeUiState
import com.pawlowski.stuboard.presentation.home.HomeViewModel
import com.pawlowski.stuboard.presentation.home.IHomeViewModel
import com.pawlowski.stuboard.ui.models.EventItemForPreview
import com.pawlowski.stuboard.ui.screens_in_bottom_navigation_related.MyGoogleMap
import com.pawlowski.stuboard.ui.theme.*
import com.pawlowski.stuboard.ui.utils.PreviewUtils
import com.pawlowski.stuboard.ui.utils.myLoadingEffect
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun HomeScreen(
    onNavigateToSearchScreen: () -> Unit = {},
    onNavigateToEventDetailScreen: (eventId: Int) -> Unit = {},
    onNavigateToMapScreen: () -> Unit = {},
    preview: Boolean = false,
    viewModel: IHomeViewModel = hiltViewModel<HomeViewModel>()
) {
    val uiState = viewModel.uiState.collectAsState()
    val suggestionsState = derivedStateOf {
        uiState.value.eventsSuggestions
    }
    val categoriesState = derivedStateOf {
        uiState.value.preferredCategories
    }
    Surface {
        LazyColumn() {

            //Google map or fake surface if preview
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                )
                {
                    MyGoogleMap(
                        modifier = Modifier
                            .height(170.dp)
                            .fillMaxWidth(),
                        preview = preview,
                        markers = PreviewUtils.defaultMarkers,
                        moveCameraToMarkersBound = true,
                        zoomButtonsEnabled = false,
                        disableAllGestures = true
                    )

                    //To make on the map clickable effect
                    Surface(modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            onNavigateToMapScreen.invoke()
                        }, color = Color(0x3F7F7F7)
                    )
                    { }
                }

            }

            //SearchCardButton
            item {
                SearchCardButton(
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 20.dp)
                )
                {
                    onNavigateToSearchScreen.invoke()
                }
            }

            //CategoriesRow
            item {
                CategoriesRow(categories = categoriesState.value)
                {
                    //TODO: Navigate only when changing filters done to avoid possible more network calls
                    viewModel.onAction(HomeUiAction.ClearAllFiltersAndSelectFilter(it))
                    onNavigateToSearchScreen.invoke()
                }
            }

            //Events suggestions
            items(items = suggestionsState.value)
            {
                LabelsRow(
                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 5.dp),
                    label1 = it.suggestionType,
                    label2 = "Więcej"
                ) {
                    it.suggestionFilters?.let { filtersList ->
                        filtersList.forEach { filter ->
                            viewModel.onAction(HomeUiAction.ClearAllFiltersAndSelectFilter(filter))
                        }


                    }
                    onNavigateToSearchScreen.invoke()
                }
                EventsRow(eventItemsForPreview = it.events, isLoading = it.isLoading)
                { eventId ->
                    onNavigateToEventDetailScreen.invoke(eventId)
                }
            }


            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )
            }

        }
    }

}


@Composable
fun SearchCardButton(modifier: Modifier = Modifier, onClick: () -> Unit) {


    Card(elevation = 8.dp,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(0.3.dp, MidGrey),
        backgroundColor = LightGray,
        modifier = modifier
            .fillMaxWidth()
            .height(55.dp)
            .clickable {
                onClick.invoke()
            }

    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.padding(horizontal = 15.dp),
                painter = painterResource(id = R.drawable.search_icon),
                contentDescription = ""
            )
            Text(
                text = stringResource(id = R.string.search_for_events),
                fontWeight = FontWeight.Normal,
                color = LighterMidGrey,
                fontFamily = montserratFont
            )
        }
    }
}

@Composable
fun CategoriesRow(
    modifier: Modifier = Modifier,
    categories: List<FilterModel.Category>,
    onCategoryClicked: (category: FilterModel) -> Unit = {}
) {

    LazyRow(modifier = modifier)
    {
        items(categories)
        {
            CategoryCard(
                modifier = Modifier.padding(start = 10.dp),
                imageId = it.categoryDrawable,
                tittle = it.tittle
            )
            {
                onCategoryClicked.invoke(it)
            }
        }

    }
}

@Composable
fun LabelsRow(
    modifier: Modifier = Modifier,
    label1: String,
    label2: String,
    onLabel2Click: () -> Unit
) {
    Row(modifier = modifier) {
        Box(modifier = Modifier.weight(1f)) {
            Text(
                text = label1,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(5.dp),
                fontFamily = montserratFont,
                fontSize = 14.sp,
            )
        }
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
            Text(
                text = label2, modifier = Modifier
                    .clickable {
                        onLabel2Click.invoke()
                    }
                    .padding(5.dp),
                fontWeight = FontWeight.Medium,
                fontFamily = montserratFont,
                textDecoration = TextDecoration.Underline,
                color = Green,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun CategoryCard(
    modifier: Modifier = Modifier,
    imageId: Int,
    tittle: String,
    onCardClick: () -> Unit
) {
    Card(shape = RoundedCornerShape(8.dp), modifier = modifier
        .width(115.dp)
        .height(75.dp)
        .clickable {
            onCardClick.invoke()
        }
    ) {
        Box {
            Image(
                painter = painterResource(id = imageId),
                contentScale = ContentScale.FillBounds,
                contentDescription = ""
            )
            Text(
                text = tittle, color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 3.dp),
                fontWeight = FontWeight.Bold,
                fontFamily = montserratFont,
                fontSize = 14.sp
            )
        }

    }
}


@Composable
fun EventCard(
    modifier: Modifier = Modifier,
    eventItemForPreview: EventItemForPreview,
    isLoading: Boolean = false,
    onCardClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .width(166.dp)
            .height(177.dp)
            .clickable(enabled = !isLoading) { onCardClick.invoke() },
        elevation = 5.dp
    ) {
        Column(
            modifier = Modifier.padding(top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                modifier = Modifier
                    .padding(bottom = 7.dp)
                    .width(150.dp)
                    .height(100.dp)
                    .myLoadingEffect(isLoading),
                model = eventItemForPreview.imageUrl,
                contentDescription = "",
                contentScale = ContentScale.FillBounds
            )

            Text(
                text = eventItemForPreview.tittle,
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .fillMaxWidth()
                    .myLoadingEffect(isLoading),
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )

            Box(
                contentAlignment = Alignment.BottomCenter, modifier = Modifier
                    .fillMaxHeight()
                    .padding(bottom = 5.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .fillMaxWidth()
                        .myLoadingEffect(isLoading),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = eventItemForPreview.place,
                        fontWeight = FontWeight.Light,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = eventItemForPreview.dateDisplayString,
                        fontWeight = FontWeight.Normal,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

        }
    }
}

@Composable
fun EventsRow(
    modifier: Modifier = Modifier,
    eventItemsForPreview: List<EventItemForPreview>,
    isLoading: Boolean = false,
    onEventCardClick: (eventId: Int) -> Unit
) {
    val eventsToDisplay = remember(eventItemsForPreview) {
        //If empty, generates empty event to display loading effect
        eventItemsForPreview.ifEmpty {
            listOf(
                EventItemForPreview(),
                EventItemForPreview(),
                EventItemForPreview()
            )
        }
    }
    LazyRow(modifier = modifier)
    {
        items(eventsToDisplay)
        {
            EventCard(
                modifier = Modifier.padding(start = 10.dp),
                eventItemForPreview = it,
                isLoading = isLoading
            )
            {
                onEventCardClick.invoke(it.eventId)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    StuboardTheme {
        HomeScreen(preview = true, viewModel = object : IHomeViewModel
        {
            override val uiState: StateFlow<HomeUiState> = MutableStateFlow(HomeUiState(
                preferredCategories = PreviewUtils.defaultFilters.filterIsInstance<FilterModel.Category>(),
                eventsSuggestions = PreviewUtils.defaultHomeEventsSuggestions
            ))

            override fun onAction(homeUiAction: HomeUiAction) {

            }

        })
    }
}