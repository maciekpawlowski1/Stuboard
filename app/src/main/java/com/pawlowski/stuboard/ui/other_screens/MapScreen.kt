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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.rememberCameraPositionState
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.ui.models.EventItemForMapScreen
import com.pawlowski.stuboard.ui.models.EventMarker
import com.pawlowski.stuboard.ui.screens_in_bottom_navigation_related.MyGoogleMap
import com.pawlowski.stuboard.ui.theme.LightGray
import com.pawlowski.stuboard.ui.theme.MidGrey
import com.pawlowski.stuboard.ui.theme.Orange
import com.pawlowski.stuboard.ui.theme.montserratFont
import com.pawlowski.stuboard.ui.utils.PreviewUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MapScreen(
    preview: Boolean = false,
    onNavigateBack: () -> Unit = {},
    onNavigateToEventDetailsScreen: (eventId: Int) -> Unit = {}
) {
    BackHandler(onBack = onNavigateBack)

    val events = PreviewUtils.defaultEventItemsForMap
    var selectedEventId by remember {
        mutableStateOf(events.getOrNull(0)?.eventId)
    }


    //TODO: Increase performance and don't recompose the map every time
    val markers = remember(key1 = events, key2 = selectedEventId) {
        events.map {
            EventMarker(
                position = it.position,
                iconId = if (it.eventId == selectedEventId)
                    it.mainCategoryDrawableIdWhenSelected
                else
                    it.mainCategoryDrawableId,
                eventTittle = it.tittle,
                eventId = it.eventId
            )
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {

        FiltersHeader(
            "Kraków",
            listOf("Naukowe", "Koncerty", "Na zewnątrz"),
            onBackClick = { onNavigateBack.invoke() }
        )

        Box(modifier = Modifier.fillMaxSize())
        {

            val coroutineScope = rememberCoroutineScope()
            val pagerState = rememberPagerState()

            val cameraPositionState = rememberCameraPositionState()
            MyGoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                preview = preview,
                markers = markers,
                locationButtonsEnabledWithAskingPermission = true,
                moveCameraToMarkersBound = false,
                onMarkerClick = { marker ->
                    selectedEventId = marker.eventId
                    val selectedIndex = events.indexOfFirst { it.eventId == selectedEventId }
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(selectedIndex)
                    }

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
                pagerState = pagerState,
            )
            { pageIndex, changesCount ->
                val newEvent = events.getOrNull(pageIndex)
                selectedEventId = newEvent?.eventId
                newEvent?.let {
                    coroutineScope.launch {
                        if(changesCount == 0)
                            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(it.position, 12f))
                        else
                            cameraPositionState.animate(CameraUpdateFactory.newLatLng(it.position))
                    }
                }

            }
        }

    }

}

@Composable
fun FiltersHeader(
    cityFilter: String,
    categoryFilters: List<String>,
    onCardClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onTuneFiltersClick: () -> Unit = {}
) {
    val filtersText = if (categoryFilters.isEmpty())
        "Wydarzenia w $cityFilter"
    else {
        val categoriesAppend = categoryFilters.reduce { acc, s ->
            "$acc - $s"
        }

        "$cityFilter - $categoriesAppend"
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


@OptIn(ExperimentalPagerApi::class)
@Composable
fun EventsPager(
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState(),
    events: List<EventItemForMapScreen>,
    onEventCardClick: (eventId: Int) -> Unit = {},
    onPageChanged: (pageIndex: Int, changesCount: Int) -> Unit = {_,_->},
) {
    HorizontalPager(
        modifier = modifier,
        count = events.size,
        state = pagerState
    ) { page ->
        val event = events[page]
        PagerEventCard(event = event, modifier = Modifier.clickable {
            onEventCardClick.invoke(event.eventId)
        })
    }

    val pageIndex = pagerState.currentPage
    val changesCount = remember {
        mutableStateOf(0)
    }
    LaunchedEffect(key1 = pageIndex)
    {
        onPageChanged.invoke(pageIndex, changesCount.value++)
    }
}

@Composable
fun PagerEventCard(modifier: Modifier = Modifier, event: EventItemForMapScreen) {
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
                    text = event.dateDisplayString,
                    color = Orange,
                    fontFamily = montserratFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                )
                Text(
                    modifier = Modifier.padding(top = 5.dp),
                    text = event.tittle,
                    fontFamily = montserratFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                )
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = event.place,
                    fontFamily = montserratFont,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                )
            }
            AsyncImage(
                modifier = Modifier
                    .width(115.dp)
                    .height(77.dp),
                model = event.imageUrl,
                contentDescription = "",
                contentScale = ContentScale.FillBounds
            )

            Icon(
                painter = painterResource(id = R.drawable.arrow_right_icon),
                contentDescription = ""
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MapScreenPreview() {
    MapScreen(preview = true)
}