package com.pawlowski.stuboard.ui.other_screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import com.pawlowski.stuboard.ui.screens_in_bottom_navigation_related.screens.FilterLabelBox
import com.pawlowski.stuboard.ui.theme.Green
import com.pawlowski.stuboard.ui.theme.LightGray
import com.pawlowski.stuboard.ui.theme.montserratFont
import com.pawlowski.stuboard.ui.utils.VerticalDivider

@Composable
fun FiltersScreen(onNavigateBack: () -> Unit = {}) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            SearchBar(onBackClick = {
                onNavigateBack.invoke()
            })
            CurrentFilters(filters = listOf("Kraków", "Naukowe", "Ten tydzień"))

            FiltersToChooseRow(
                modifier = Modifier.padding(vertical = 5.dp),
                filterName = "Kategorie",
                filters = listOf("Naukowe", "Koncerty", "Sportowe", "Biznesowe", "Na zewnątrz")
            )

            FiltersToChooseRow(
                modifier = Modifier.padding(vertical = 5.dp),
                filterName = "Miejsce",
                filters = listOf("Online", "Katowice", "Warszawa", "Gliwice","Szczecin", "Gdańsk")
            )

            FiltersToChooseRow(
                modifier = Modifier.padding(vertical = 5.dp),
                filterName = "Cena",
                filters = listOf("Za darmo", "<25zł","<50zł", "<100zł", "<200zł","<300zł")
            )

            FiltersToChooseRow(
                modifier = Modifier.padding(vertical = 5.dp),
                filterName = "Czas",
                filters = listOf("Dzisiaj", "Jutro","Ten tydzień", "Do 2 tyg.", "Do 4 tyg.")
            )
        }
    }

}

@Composable
fun FiltersToChooseRow(
    modifier: Modifier = Modifier,
    startPadding: Dp = 8.dp,
    endPadding: Dp = 8.dp,
    spaceBetweenFilters: Dp = 8.dp,
    filterName: String,
    filters: List<String>,
    onFilterChosen: () -> Unit = {},
    onSeeMoreClick: () -> Unit = {}
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.padding(start = startPadding),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Text(
                text = filterName,
                fontFamily = montserratFont,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd)
            {
                Text(
                    modifier = Modifier
                        .clickable { onSeeMoreClick.invoke() }
                        .padding(vertical = 5.dp, horizontal = endPadding),
                    text = "Zobacz więcej",
                    fontFamily = montserratFont,
                    color = Green,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }

        }
        LazyRow(modifier = Modifier.padding(top = 5.dp))
        {
            item {
                Spacer(modifier = Modifier.width(startPadding))
            }
            items(filters)
            {
                FilterLabelBox(
                    modifier = Modifier
                        .padding(end = spaceBetweenFilters)
                        .clickable { onFilterChosen.invoke() },
                    text = it
                )
            }
        }
    }
}



@Composable
fun CurrentFilters(filters: List<String>) {
    Surface(modifier = Modifier.fillMaxWidth(), color = LightGray) {
        Column {
            Text(
                modifier = Modifier.padding(5.dp),
                text = "Wybrane:",
                fontFamily = montserratFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
            FlowRow(
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp, bottom = 10.dp),
                mainAxisSpacing = 5.dp
            ) {
                filters.forEach {
                    CancellableFilterLabel(filter = it)
                }
            }
        }
    }
}

@Composable
fun CancellableFilterLabel(filter: String) {
    Card(shape = RectangleShape, border = BorderStroke(width = 0.5.dp, color = Green)) {
        Row(modifier = Modifier.height(27.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 4.dp),
                text = filter,
                color = Green,
                fontFamily = montserratFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
            )
            VerticalDivider(color = Green, thickness = 0.3.dp)
            Icon(modifier = Modifier
                .clickable { }
                .fillMaxHeight()
                .padding(vertical = 3.dp),
                painter = painterResource(id = R.drawable.close_icon),
                contentDescription = "",
                tint = Green)
        }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier, onBackClick: () -> Unit = {}) {
    Card(
        modifier = modifier
            .height(68.dp)
            .fillMaxWidth(),
        shape = RectangleShape,
        elevation = 13.dp
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier
                    .clickable { onBackClick.invoke() }
                    .padding(10.dp),
                painter = painterResource(id = R.drawable.arrow_back_icon),
                contentDescription = ""
            )
            VerticalDivider(color = Color.Black, thickness = 0.3.dp)
            Text(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .weight(1f), text = "Wpisz aby wyszukać"
            )
            SearchIconBox()
        }
    }
}

@Composable
fun SearchIconBox() {
    Surface(
        modifier = Modifier
            .fillMaxHeight()
            .width(65.dp), color = Green
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(id = R.drawable.search_icon),
                contentDescription = "",
                tint = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FiltersScreenPreview() {
    FiltersScreen()
}