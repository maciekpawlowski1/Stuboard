package com.pawlowski.stuboard.ui.other_screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.rememberCameraPositionState
import com.pawlowski.stuboard.ui.models.EventItemForMapScreen
import com.pawlowski.stuboard.ui.models.EventItemForPreview
import com.pawlowski.stuboard.ui.models.EventMarker
import com.pawlowski.stuboard.ui.screens_in_bottom_navigation_related.MyGoogleMap
import com.pawlowski.stuboard.ui.screens_in_bottom_navigation_related.screens.EventCard
import com.pawlowski.stuboard.ui.utils.PreviewUtils
import kotlinx.coroutines.launch

@Composable
fun MapScreen(preview: Boolean = false)
{
    val events = PreviewUtils.defaultEventItemsForMap
    var selectedEventId by remember {
        mutableStateOf(events.getOrNull(0)?.eventId)
    }

    //TODO: Increase performance and don't recompose the map every time
    val markers = remember(key1 = events, key2 = selectedEventId) {
        events.map {
            EventMarker(
                position = it.position,
                iconId = if(it.eventId == selectedEventId)
                    it.mainCategoryDrawableIdWhenSelected
                else
                    it.mainCategoryDrawableId,
                eventTittle = it.tittle
            )
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Surface(modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)) {

        }
        Box(modifier = Modifier.fillMaxSize())
        {
            val cameraPositionState = rememberCameraPositionState()
            MyGoogleMap(
                modifier = Modifier.fillMaxSize(),
                preview = preview,
                markers = markers,
                locationButtonsEnabledWithAskingPermission = true,
                moveCameraToMarkersBound = true,
                cameraPositionState = cameraPositionState
                )

            val coroutineScope = rememberCoroutineScope()

            EventsPager(modifier = Modifier
                .height(200.dp)
                .align(Alignment.BottomCenter), events = events)
            { pageIndex ->
                val newEvent = events.getOrNull(pageIndex)
                selectedEventId = newEvent?.eventId
                newEvent?.let {
                    coroutineScope.launch {
                        cameraPositionState.animate(CameraUpdateFactory.newLatLng(it.position))
                    }
                }

            }
        }

    }

}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun EventsPager(modifier: Modifier = Modifier, events: List<EventItemForMapScreen>, onPageChanged: (pageIndex: Int) -> Unit = {})
{
    val pagerState = rememberPagerState()
    HorizontalPager(modifier = modifier,
        count = events.size,
        state = pagerState
    ) { page ->
        val event = events[page]
        val previewEvent = EventItemForPreview(
            eventId = event.eventId,
            tittle = event.tittle,
            place = event.place,
            dateDisplayString = event.dateDisplayString,
            isFree = event.isFree,
            imageUrl = event.imageUrl
            )
        EventCard(eventItemForPreview = previewEvent, padding = PaddingValues()) {

        }
    }

    val pageIndex = pagerState.currentPage
    val changesCount = remember {
        mutableStateOf(0)
    }
    LaunchedEffect(key1 = pageIndex)
    {
        if(changesCount.value != 0)
            onPageChanged.invoke(pageIndex)
        changesCount.value++
    }
}

@Preview(showBackground = true)
@Composable
fun MapScreenPreview()
{
    MapScreen(preview = true)
}