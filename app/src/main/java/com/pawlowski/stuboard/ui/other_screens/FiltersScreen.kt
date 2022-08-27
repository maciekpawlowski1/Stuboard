package com.pawlowski.stuboard.ui.other_screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowRow
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.ui.theme.Green
import com.pawlowski.stuboard.ui.theme.LightGray
import com.pawlowski.stuboard.ui.theme.montserratFont
import com.pawlowski.stuboard.ui.utils.VerticalDivider

@Composable
fun FiltersScreen(onNavigateBack: () -> Unit = {})
{
    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            SearchBar(onBackClick = {
                onNavigateBack.invoke()
            })
            CurrentFilters(filters = listOf("Kraków", "Naukowe", "Ten tydzień"))
        }
    }

}

@Composable
fun CurrentFilters(filters: List<String>)
{
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
fun CancellableFilterLabel(filter: String)
{
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
            Icon(modifier = Modifier.clickable {  }.fillMaxHeight().padding(vertical = 3.dp), painter = painterResource(id = R.drawable.close_icon), contentDescription = "", tint = Green)
        }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier, onBackClick: () -> Unit = {})
{
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
            Text(modifier = Modifier
                .padding(horizontal = 10.dp)
                .weight(1f), text = "Wpisz aby wyszukać")
            SearchIconBox()
        }
    }
}

@Composable
fun SearchIconBox()
{
    Surface(modifier = Modifier
        .fillMaxHeight()
        .width(65.dp), color = Green) {
        Box(contentAlignment = Alignment.Center) {
            Icon(painter = painterResource(id = R.drawable.search_icon), contentDescription = "", tint = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FiltersScreenPreview()
{
    FiltersScreen()
}