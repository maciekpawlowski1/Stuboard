package com.pawlowski.stuboard.ui.screens_in_bottom_navigation_related.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
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
import com.google.accompanist.flowlayout.FlowRow
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.ui.theme.GrayGreen
import com.pawlowski.stuboard.ui.theme.Green
import com.pawlowski.stuboard.ui.theme.StuboardTheme
import com.pawlowski.stuboard.ui.theme.montserratFont
import com.pawlowski.stuboard.ui.utils.PreviewUtils

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchScreen(onNavigateToEventDetailsScreen: (eventId: Int) -> Unit = {}, onNavigateToFiltersScreen: () -> Unit = {})
{
    Surface(modifier = Modifier.fillMaxHeight()) {
        Column {
            Surface(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(), color = GrayGreen) {
                Column(modifier = Modifier.padding(vertical = 30.dp)) {
                    SearchBarWithFilterValues(PaddingValues(
                        start = 20.dp,
                        end = 20.dp,
                        bottom= 10.dp
                    ), PreviewUtils.defaultFilters)

                    FiltersCard(modifier = Modifier.align(CenterHorizontally),PreviewUtils.defaultFilters.size)
                    {
                        onNavigateToFiltersScreen.invoke()
                    }

                }
            }

            val eventsCount = 12
            Text(modifier = Modifier.padding(top = 15.dp, start = 15.dp, bottom = 15.dp),
                text = "Wyniki wyszukiwania ($eventsCount):",
                fontWeight = FontWeight.SemiBold,
                fontFamily = montserratFont
            )

            LazyVerticalGrid(
                cells = GridCells.Fixed(2),
                contentPadding = PaddingValues(
                    start = 10.dp,
                    end = 10.dp,
                    bottom = 80.dp,
                )
            )
            {
                items(PreviewUtils.defaultEventPreviews)
                {
                    EventCard(modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp), eventItemForPreview = it) {
                        onNavigateToEventDetailsScreen.invoke(it.eventId)
                    }
                }
            }

        }
    }

}

@Composable
fun FiltersCard(modifier: Modifier, filtersCount: Int, onCardClick: () -> Unit = {})
{
    Card(modifier = modifier
        .height(33.dp)
        .fillMaxWidth(0.8f)
        .clickable { onCardClick.invoke() },
        shape = RectangleShape,
        elevation = 5.dp,
    ) {
        Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Center) {
            Row(verticalAlignment = CenterVertically) {
                Icon(modifier= Modifier.padding(vertical = 5.dp, horizontal = 5.dp),painter = painterResource(id = R.drawable.filter_list_icon), contentDescription = "")
                Text(
                    text = "Filtry",
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = montserratFont,
                    fontSize = 13.sp
                )
                FiltersCountCircle(padding = PaddingValues(start = 10.dp), filtersCount = filtersCount)
            }
        }
    }
}

@Composable
fun FiltersCountCircle(padding: PaddingValues = PaddingValues(), filtersCount: Int)
{
    Surface(modifier = Modifier
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
fun SearchBarWithFilterValues(paddingValues: PaddingValues, filters: List<String>, onClearClick: () -> Unit = {})
{
    Card(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth()
            .wrapContentHeight(CenterVertically),
        shape = RectangleShape,
        elevation = 4.dp

    ) {
        Row(modifier = Modifier.padding(vertical = 10.dp)) {
            FlowRow(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .weight(1f, true),
                crossAxisSpacing = 8.dp,
                mainAxisSpacing = 10.dp
            ) {
                filters.forEach {
                    FilterLabelBox(text = it)
                }

            }

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
                    painter = painterResource(id = R.drawable.close_icon),
                    contentDescription = ""
                )
            }
        }

    }
}

@Composable
fun FilterLabelBox(
    modifier: Modifier = Modifier,
    text: String,
    borderColor: Color = Green,
    borderWith: Dp = 1.3.dp,
    height: Dp = 29.dp,
)
{
    Card(modifier = modifier
        .height(height),
        shape = RectangleShape,
        border = BorderStroke(borderWith, color = borderColor)
    ) {
        Box(contentAlignment = Center)
        {
            Text(
                modifier = Modifier.padding(horizontal = 5.dp),
                text = text,
                fontFamily = montserratFont,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    StuboardTheme {
        SearchScreen {}
    }
}