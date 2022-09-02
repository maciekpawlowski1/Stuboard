package com.pawlowski.stuboard.ui.screens_in_bottom_navigation_related.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.flowlayout.FlowRow
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.presentation.search.ISearchViewModel
import com.pawlowski.stuboard.presentation.search.SearchUiAction
import com.pawlowski.stuboard.presentation.search.SearchUiState
import com.pawlowski.stuboard.presentation.search.SearchViewModel
import com.pawlowski.stuboard.ui.models.EventItemForPreview
import com.pawlowski.stuboard.ui.theme.*
import com.pawlowski.stuboard.ui.utils.PreviewUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    onNavigateToEventDetailsScreen: (eventId: Int) -> Unit = {},
    onNavigateToFiltersScreen: () -> Unit = {},
    viewModel: ISearchViewModel = hiltViewModel<SearchViewModel>(),
    onNavigateToMapScreen: () -> Unit = {}
) {
    val uiState = viewModel.uiState.collectAsState()
    val selectedFiltersState = derivedStateOf {
        uiState.value.selectedFilters
    }
    val selectedFiltersCountState = derivedStateOf {
        selectedFiltersState.value.size
    }


    Surface(modifier = Modifier.fillMaxHeight()) {
        Column {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(), color = GrayGreen
            ) {
                Column(modifier = Modifier.padding(vertical = 30.dp)) {
                    SearchBarWithFilterValues(PaddingValues(
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 10.dp
                    ),
                        filters = selectedFiltersState.value.map { it.tittle },
                        onNavigateToFiltersScreen = {
                            onNavigateToFiltersScreen.invoke()
                        })

                    FiltersCard(
                        modifier = Modifier.align(CenterHorizontally),
                        selectedFiltersCountState.value
                    )
                    {
                        onNavigateToFiltersScreen.invoke()
                    }

                }
            }

            val eventsCount = 12
            Text(
                modifier = Modifier.padding(top = 15.dp, start = 15.dp, bottom = 15.dp),
                text = "Wyniki wyszukiwania ($eventsCount):",
                fontWeight = FontWeight.SemiBold,
                fontFamily = montserratFont
            )
            val lazyPagingItems = viewModel.pagingData?.collectAsLazyPagingItems()
            if (lazyPagingItems != null) {
                Box(modifier = Modifier.fillMaxSize()) {
                    val coroutineScope = rememberCoroutineScope()
                    val lastScrollIndex = viewModel.lastSavedScrollPosition.collectAsState().value
                    val listState = rememberLazyGridState(initialFirstVisibleItemIndex = lastScrollIndex)

                    LazyVerticalGrid(
                        state = listState,
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(
                            start = 10.dp,
                            end = 10.dp,
                            bottom = 80.dp,
                        )
                    )
                    {
                        item(span = { GridItemSpan(2) }) {
                            when(lazyPagingItems.loadState.refresh)
                            {
                                is LoadState.Loading -> {
                                    Box(contentAlignment = Center) {
                                        CircularProgressIndicator(
                                            modifier = Modifier
                                                .padding(vertical = 5.dp)
                                                .size(40.dp),
                                            color = Green
                                        )
                                    }
                                }
                                is LoadState.Error -> {
                                    Button(onClick = { lazyPagingItems.retry() }, colors = ButtonDefaults.buttonColors(backgroundColor = Green)) {
                                        Text(text = "Try again", color = Color.White)
                                    }
                                }
                                else -> {}
                            }
                        }
                        items(count = lazyPagingItems.itemCount)
                        { index ->
                            lazyPagingItems[index]?.let {
                                EventCard(
                                    modifier = Modifier.padding(
                                        vertical = 10.dp,
                                        horizontal = 6.dp
                                    ), eventItemForPreview = it
                                ) {
                                    onNavigateToEventDetailsScreen.invoke(it.eventId)
                                }
                            }

                        }
                        item(span = { GridItemSpan(2) })
                        {
                            when(lazyPagingItems.loadState.append)
                            {
                                is LoadState.Loading -> {
                                    Box(contentAlignment = Center) {
                                        CircularProgressIndicator(
                                            modifier = Modifier
                                                .padding(vertical = 5.dp)
                                                .size(40.dp),
                                            color = Green
                                        )
                                    }
                                }
                                is LoadState.Error -> {
                                    Button(onClick = { lazyPagingItems.retry() }, colors = ButtonDefaults.buttonColors(backgroundColor = Green)) {
                                        Text(text = "Try again", color = Color.White)
                                    }
                                }
                                else -> {}
                            }

                        }
                    }

                    DisposableEffect(lazyPagingItems)
                    {
                        coroutineScope.launch {
                            //TODO: Find better way
                            delay(50) //Without delay doesn't work...
                            listState.scrollToItem(lastScrollIndex)
                        }

                        onDispose()
                        {
                            viewModel.onAction(SearchUiAction.SaveScrollPosition(listState.firstVisibleItemIndex))
                        }
                    }

                    Column(modifier = Modifier
                        .align(BottomCenter)
                        .width(130.dp)
                        .height(100.dp),
                        horizontalAlignment = CenterHorizontally
                        ) {
                        AnimatedVisibility(visible = !listState.isScrollInProgress) {
                            GoToMapButton()
                            {
                                onNavigateToMapScreen()
                            }
                        }

                    }

                }

            }


        }
    }

}

@Composable
fun GoToMapButton(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Card(
        modifier = modifier
            .wrapContentSize()
            .clickable { onClick.invoke() },
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Green
    ) {
        Row(verticalAlignment = CenterVertically) {
            Icon(
                modifier= Modifier.padding(vertical = 1.dp, horizontal = 3.dp),
                painter = painterResource(id = R.drawable.map_icon),
                contentDescription = "",
                tint = Color.White
            )
            Text(
                modifier= Modifier.padding(end = 5.dp),
                text = "Go to map",
                color = Color.White,
                fontFamily = montserratFont,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun FiltersCard(modifier: Modifier, filtersCount: Int, onCardClick: () -> Unit = {}) {
    Card(
        modifier = modifier
            .height(33.dp)
            .fillMaxWidth(0.8f)
            .clickable { onCardClick.invoke() },
        shape = RectangleShape,
        elevation = 5.dp,
    ) {
        Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Center) {
            Row(verticalAlignment = CenterVertically) {
                Icon(
                    modifier = Modifier.padding(vertical = 5.dp, horizontal = 5.dp),
                    painter = painterResource(id = R.drawable.filter_list_icon),
                    contentDescription = ""
                )
                Text(
                    text = "Filtry",
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = montserratFont,
                    fontSize = 13.sp
                )
                FiltersCountCircle(
                    padding = PaddingValues(start = 10.dp),
                    filtersCount = filtersCount
                )
            }
        }
    }
}

@Composable
fun FiltersCountCircle(padding: PaddingValues = PaddingValues(), filtersCount: Int) {
    Surface(
        modifier = Modifier
            .padding(padding)
            .width(25.dp)
            .height(25.dp),
        shape = CircleShape,
        color = Green
    ) {
        Box(contentAlignment = Center) {
            Text(
                text = "$filtersCount",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontFamily = montserratFont,
                fontSize = 15.sp
            )
        }

    }
}

@Composable
fun SearchBarWithFilterValues(
    paddingValues: PaddingValues,
    filters: List<String>,
    onClearClick: () -> Unit = {},
    onNavigateToFiltersScreen: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth()
            .wrapContentHeight(CenterVertically),
        shape = RectangleShape,
        elevation = 4.dp

    ) {
        Row(
            verticalAlignment = CenterVertically
        ) {
            Box(modifier = Modifier
                .weight(1f)
                .clickable { onNavigateToFiltersScreen.invoke() }
                .padding(vertical = 10.dp))
            {
                Row(verticalAlignment = CenterVertically) {
                    if (filters.isEmpty()) {
                        Box(contentAlignment = Center,
                            modifier = Modifier
                                .clickable { onClearClick.invoke() }
                                .padding(
                                    vertical = 10.dp,
                                    horizontal = 10.dp
                                )
                                .wrapContentWidth()
                        )
                        {
                            Icon(
                                painter = painterResource(id = R.drawable.search_icon),
                                contentDescription = ""
                            )
                        }
                        Text(
                            text = "Wpisz aby wyszukać",
                            color = MidGrey,
                            fontFamily = montserratFont,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        )
                    } else {
                        FlowRow(
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .weight(1f, true),
                            crossAxisSpacing = 8.dp,
                            mainAxisSpacing = 10.dp,
                        ) {
                            filters.forEach {
                                FilterLabelBox(
                                    text = {
                                        Text(
                                            modifier = Modifier.padding(horizontal = 5.dp),
                                            text = it,
                                            fontFamily = montserratFont,
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 13.sp
                                        )
                                    }
                                )
                            }

                        }

                    }
                }
            }

            if (filters.isNotEmpty()) {

                Box(contentAlignment = Center,
                    modifier = Modifier
                        .wrapContentHeight()
                        .clickable { onClearClick.invoke() }
                        .padding(
                            vertical = 10.dp,
                            horizontal = 10.dp
                        )
                        .wrapContentWidth()
                )
                {
                    Icon(
                        painter = painterResource(id = R.drawable.close_icon),
                        contentDescription = ""
                    )
                }
            }

        }

    }
}

@Composable
fun FilterLabelBox(
    modifier: Modifier = Modifier,
    text: @Composable () -> Unit = { },
    borderColor: Color = Green,
    borderWith: Dp = 1.3.dp,
    height: Dp = 29.dp,
    icon: @Composable () -> Unit = { },
) {
    Card(
        modifier = modifier
            .height(height),
        shape = RectangleShape,
        border = BorderStroke(borderWith, color = borderColor)
    ) {
        Box(contentAlignment = CenterStart)
        {
            Row(verticalAlignment = CenterVertically) {
                icon.invoke()
                text.invoke()
            }

        }

    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    StuboardTheme {
        SearchScreen(viewModel = object : ISearchViewModel {
            override val uiState: StateFlow<SearchUiState> = MutableStateFlow(
                SearchUiState(
                    selectedFilters = PreviewUtils.defaultFilters,
                )
            )
            override val pagingData: Flow<PagingData<EventItemForPreview>>?
                get() = null

            override val lastSavedScrollPosition: StateFlow<Int> = MutableStateFlow(0)

            override fun onAction(action: SearchUiAction) {
                TODO("Not yet implemented")
            }

        })
    }
}