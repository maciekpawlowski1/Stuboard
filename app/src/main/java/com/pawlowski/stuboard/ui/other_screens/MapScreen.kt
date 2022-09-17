package com.pawlowski.stuboard.ui.other_screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.presentation.filters.FilterModel
import com.pawlowski.stuboard.presentation.map.IMapMviViewModel
import com.pawlowski.stuboard.presentation.map.MapMviViewModel
import com.pawlowski.stuboard.presentation.map.MapSingleEvent
import com.pawlowski.stuboard.presentation.map.MapUiState
import com.pawlowski.stuboard.ui.models.EventItemForMapScreen
import com.pawlowski.stuboard.ui.screens_in_bottom_navigation_related.MyGoogleMap
import com.pawlowski.stuboard.ui.theme.LightGray
import com.pawlowski.stuboard.ui.theme.MidGrey
import com.pawlowski.stuboard.ui.theme.Orange
import com.pawlowski.stuboard.ui.theme.montserratFont
import com.pawlowski.stuboard.ui.utils.PreviewUtils
import com.pawlowski.stuboard.ui.utils.myLoadingEffect
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MapScreen(
    preview: Boolean = false,
    onNavigateBack: () -> Unit = {},
    onNavigateToEventDetailsScreen: (eventId: String) -> Unit = {},
    viewModel: IMapMviViewModel = hiltViewModel<MapMviViewModel>()
) {
    BackHandler(onBack = onNavigateBack)


    val pagerState = rememberPagerState()

    val cameraPositionState = rememberCameraPositionState()
    {
        position = CameraPosition.fromLatLngZoom(LatLng(50.0624, 19.9116), 12f)
    }
    val mapsAnimationScope = rememberCoroutineScope()
    val pagerAnimationScope = rememberCoroutineScope()
    LaunchedEffect(true) {
        viewModel.container.sideEffectFlow.collect { event ->
            when(event) {
                is MapSingleEvent.AnimatedMoveMapToPosition -> {
                    mapsAnimationScope.launch(SupervisorJob()) {
                        cameraPositionState.animate(CameraUpdateFactory.newLatLng(event.position))
                    }

                }
                is MapSingleEvent.AnimatedScrollToPage -> {
                    if(pagerAnimationScope.isActive)
                        pagerAnimationScope.coroutineContext.cancelChildren()
                    pagerAnimationScope.launch(SupervisorJob()) {
                        pagerState.animateScrollToPage(event.pageIndex)
                    }

                }
            }
        }
    }

    val uiState = viewModel.container.stateFlow.collectAsState()
    val eventsState = derivedStateOf {
        val uiStateValue = uiState.value
        if(uiStateValue is MapUiState.Success)
            uiStateValue.events
        else
            listOf()
    }
    val currentFiltersState = derivedStateOf {
        uiState.value.currentFilters
    }

    val isLoadingState = derivedStateOf {
        uiState.value is MapUiState.Loading
    }

    val events = eventsState.value

    val markersState = derivedStateOf {
        when(val uiStateValue = uiState.value) {
            is MapUiState.Success -> uiStateValue.markers
            else -> listOf()
        }
    }



    Column(modifier = Modifier.fillMaxSize()) {

        FiltersHeader(
            currentFiltersState.value,
            onBackClick = { onNavigateBack.invoke() }
        )

        Box(modifier = Modifier.fillMaxSize())
        {

            MyGoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                preview = preview,
                markers = markersState.value,
                locationButtonsEnabledWithAskingPermission = true,
                moveCameraToMarkersBound = false,
                onMarkerClick = { marker ->
                    viewModel.onEventSelected(marker.eventId)
                }
            )



            EventsPager(
                modifier = Modifier
                    .height(200.dp)
                    .align(Alignment.BottomCenter),
                events = events,
                onEventCardClick = {
                   onNavigateToEventDetailsScreen.invoke(it)
                },
                isLoading = isLoadingState.value,
                pagerState = pagerState,
            )
            { pageIndex ->
                viewModel.onPageChanged(pageIndex)
            }
        }

    }

}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun EventsPager(
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState(),
    events: List<EventItemForMapScreen>,
    isLoading: Boolean = false,
    onEventCardClick: (eventId: String) -> Unit = {},
    onPageChanged: (pageIndex: Int) -> Unit = {},
) {
    HorizontalPager(
        modifier = modifier,
        count =
        if(isLoading)
            1
        else
            events.size,
        state = pagerState
    ) { page ->
        val event = if(isLoading)
            PreviewUtils.defaultEventItemsForMap[0] //To show some default shape of event
        else events[page]
        PagerEventCard(event = event, isLoading = isLoading, modifier = Modifier.clickable {
            onEventCardClick.invoke(event.eventId)
        })
    }

    val pageIndex = pagerState.currentPage
    val isScrolling = pagerState.isScrollInProgress

    LaunchedEffect(pageIndex, isScrolling)
    {
        if(!isScrolling)
            onPageChanged.invoke(pageIndex)
    }
}


@Composable
fun FiltersHeader(
    filters: List<FilterModel>,
    onCardClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onTuneFiltersClick: () -> Unit = {}
) {
    val filtersText = if(filters.isEmpty())
        "Loading..."
    else if (filters.size == 1)
        "Wydarzenia w ${filters[0].tittle}"
    else {
        val categoriesAppend = filters.map { it.tittle }.reduce { acc, s ->
            "$acc - $s"
        }

        categoriesAppend
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
    ) {
        Box(contentAlignment = Alignment.Center)
        {
            Row(modifier = Modifier.padding(horizontal = 15.dp)) {
                Card(
                    modifier = Modifier
                        .clickable { onCardClick.invoke() }
                        .padding(end = 10.dp)
                        .height(40.dp)
                        .weight(1f),
                    backgroundColor = LightGray
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            modifier = Modifier
                                .clickable { onBackClick.invoke() }
                                .padding(vertical = 4.dp),
                            painter = painterResource(id = R.drawable.arrow_back_icon),
                            contentDescription = ""
                        )
                        Box(modifier = Modifier
                            .fillMaxHeight()
                            .clickable { onCardClick.invoke() }
                            .padding(horizontal = 5.dp)
                            .weight(1f),
                            contentAlignment = Alignment.CenterStart
                        )
                        {
                            Text(
                                modifier = Modifier,
                                text = filtersText,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                fontFamily = montserratFont,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                color = MidGrey
                            )
                        }


                    }
                }
                Card(backgroundColor = LightGray) {
                    Icon(
                        modifier = Modifier
                            .clickable { onTuneFiltersClick.invoke() }
                            .padding(4.dp)
                            .height(30.dp),
                        painter = painterResource(id = R.drawable.tune_icon),
                        contentDescription = ""
                    )
                }
            }


        }
    }
}

@Composable
fun PagerEventCard(modifier: Modifier = Modifier, event: EventItemForMapScreen, isLoading: Boolean = false) {
    Card(
        modifier = modifier
            .width((LocalConfiguration.current.screenWidthDp - 30).dp)
            .height(150.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    modifier= Modifier
                        .myLoadingEffect(isLoading),
                    text = event.dateDisplayString,
                    color = Orange,
                    fontFamily = montserratFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                )
                Text(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .myLoadingEffect(isLoading),
                    text = event.tittle,
                    fontFamily = montserratFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                )
                Text(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .myLoadingEffect(isLoading),
                    text = event.place,
                    fontFamily = montserratFont,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                )
            }
            AsyncImage(
                modifier = Modifier
                    .padding(
                        end = if (isLoading)
                            20.dp
                        else
                            0.dp
                    )
                    .width(115.dp)
                    .height(77.dp)
                    .myLoadingEffect(isLoading),
                model = event.imageUrl,
                contentDescription = "",
                contentScale = ContentScale.FillBounds
            )

            if(!isLoading)
            {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_right_icon),
                    contentDescription = ""
                )
            }

        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun MapScreenPreview() {
//    MapScreen(preview = true, viewModel = object: IMapViewModel
//    {
//        override val uiState: StateFlow<MapUiState> = MutableStateFlow(
//            MapUiState.Success(
//                events = PreviewUtils.defaultEventItemsForMap,
//                _currentFilters = PreviewUtils.defaultFilters
//            )
//        )
//    })
//}