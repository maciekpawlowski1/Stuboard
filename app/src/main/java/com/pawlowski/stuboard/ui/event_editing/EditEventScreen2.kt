package com.pawlowski.stuboard.ui.event_editing

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.ui.components.FilterLabelBox
import com.pawlowski.stuboard.ui.components.MySwitch
import com.pawlowski.stuboard.ui.components.SwitchButtonType
import com.pawlowski.stuboard.ui.theme.montserratFont

@Composable
fun EditEventScreen2() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            modifier = Modifier.align(CenterHorizontally),
            text = "Kategorie:",
            fontFamily = montserratFont,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
        Spacer(modifier = Modifier.height(10.dp))



        CategoriesRow()
        Spacer(modifier = Modifier.height(10.dp))
        TwoColumnsFiltersRow(
            label = "Dostęp:",
            firstFilterText = "Dla wszystkich",
            secondFilterText = "Dostęp ograniczony",
            onFirstFilterClick = {},
            onSecondFilterClick = {}
        )

        Spacer(modifier = Modifier.height(10.dp))
        TwoColumnsFiltersRow(
            label = "Rejestracja:",
            firstFilterText = "Bez rejestracji",
            secondFilterText = "Wymaga rejestracji",
            onFirstFilterClick = {},
            onSecondFilterClick = {}
        )

        Spacer(modifier = Modifier.height(10.dp))
        TwoColumnsFiltersRow(
            label = "Cena:",
            firstFilterText = "Darmowe",
            secondFilterText = "Płatne",
            onFirstFilterClick = {},
            onSecondFilterClick = {}
        )

        Spacer(modifier = Modifier.height(10.dp))
        TwoColumnsFiltersRow(
            label = "Budynek:",
            firstFilterText = "Na zewnątrz",
            secondFilterText = "W środku",
            onFirstFilterClick = {},
            onSecondFilterClick = {}
        )

        Spacer(modifier = Modifier.height(20.dp))


        HeaderText(text = "Miejsce:", modifier = Modifier.align(CenterHorizontally))
        Spacer(modifier = Modifier.height(10.dp))
        MySwitch(
            selectedButton = SwitchButtonType.FIRST,
            firstButtonLabel = "Online",
            secondButtonLabel = "Stacjonarnie"
        )
    }
}



@Composable
private fun TwoColumnsFiltersRow(
    label: String,
    firstFilterText: String,
    secondFilterText: String,
    onFirstFilterClick: () -> Unit,
    onSecondFilterClick: () -> Unit
)
{
    HeaderText(text = label)
    Spacer(modifier = Modifier.height(10.dp))
    Row {
        FilterLabelBox(
            modifier = Modifier
                .weight(1f)
                .clickable { onFirstFilterClick() },
            text = {
                Text(
                    modifier = Modifier.padding(end = 5.dp),
                    text = firstFilterText,
                    fontFamily = montserratFont,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp
                )
            },
            contentAlignment = Center
        )

        Spacer(modifier = Modifier.width(15.dp))

        FilterLabelBox(
            modifier = Modifier
                .weight(1f)
                .clickable { onSecondFilterClick() },
            text = {
                Text(
                    modifier = Modifier.padding(end = 5.dp),
                    text = secondFilterText,
                    fontFamily = montserratFont,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp
                )
            },
            contentAlignment = Center
        )

    }
}


@Composable
private fun HeaderText(modifier: Modifier = Modifier, text: String)
{
    Text(
        modifier = modifier,
        text = text,
        fontFamily = montserratFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp
    )

}

@Composable
private fun CategoriesRow()
{
    HeaderText(text = "Główna:")
    Spacer(modifier = Modifier.height(10.dp))
    LazyVerticalGrid(columns = GridCells.Fixed(3))
    {
        items(6)
        {
            FilterLabelBox(
                modifier = Modifier.padding(3.dp),
                text = {
                    Text(
                        modifier = Modifier.padding(end = 5.dp),
                        text = "Koncerty",
                        fontFamily = montserratFont,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp
                    )
                },
                icon = {
                    Icon(
                        modifier = Modifier.padding(3.dp),
                        painter = painterResource(id = R.drawable.guitar_icon),
                        contentDescription = ""
                    )
                })
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun EditEventScreen2Preview() {
    EditEventScreen2()
}