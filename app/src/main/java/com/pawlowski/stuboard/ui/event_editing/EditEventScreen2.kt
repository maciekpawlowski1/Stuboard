package com.pawlowski.stuboard.ui.event_editing

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.pawlowski.stuboard.presentation.filters.FilterModel
import com.pawlowski.stuboard.presentation.filters.FilterType
import com.pawlowski.stuboard.ui.components.FilterLabelBox
import com.pawlowski.stuboard.ui.components.MySwitch
import com.pawlowski.stuboard.ui.components.SwitchButtonType
import com.pawlowski.stuboard.ui.theme.Green
import com.pawlowski.stuboard.ui.theme.Orange
import com.pawlowski.stuboard.ui.theme.montserratFont

@Composable
fun EditEventScreen2(
    isOnlineSelected: () -> Boolean = {false},
    onIsOnlineSelectedChange: (Boolean) -> Unit = {},
    categories: () -> Map<FilterType, Map<FilterModel, Boolean>> = { mapOf() },
    onCategorySelectionChange: (FilterModel, Boolean) -> Unit = {_,_->}
) {
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

        val categoriesValue = categories()

        categoriesValue[FilterType.CATEGORY]?.let {
            CategoriesRow(categories = it.mapKeys { entry -> entry.key as FilterModel.Category },
            onCategorySelectionChange = { filter, isSelected ->
                onCategorySelectionChange(filter, isSelected)
            })
        }
        Spacer(modifier = Modifier.height(10.dp))
        val otherEntries = categoriesValue.entries.filter {
            it.key != FilterType.CATEGORY
        }

        otherEntries.forEach {
            val filters = it.value.entries.toList()
            val first = filters.getOrNull(0)
            val second = filters.getOrNull(1)
            if(first != null && second != null)
            {
                TwoColumnsFiltersRow(
                    label = "${it.key.typeString}:",
                    firstFilterText = first.key.tittle,
                    secondFilterText = second.key.tittle,
                    onFirstFilterClick = {
                        if(second.value)
                            onCategorySelectionChange(second.key, false)
                        onCategorySelectionChange(first.key, !first.value)
                                         },
                    onSecondFilterClick = {
                        if(first.value)
                            onCategorySelectionChange(first.key, false)
                        onCategorySelectionChange(second.key, !second.value)
                    },
                    isFirstSelected = first.value,
                    isSecondSelected = second.value
                )
                Spacer(modifier = Modifier.height(10.dp))

            }

        }


        Spacer(modifier = Modifier.height(20.dp))


        HeaderText(text = "Miejsce:", modifier = Modifier.align(CenterHorizontally))
        Spacer(modifier = Modifier.height(10.dp))
        MySwitch(
            selectedButton = if(isOnlineSelected())
                SwitchButtonType.FIRST
            else
                SwitchButtonType.SECOND,
            firstButtonLabel = "Online",
            secondButtonLabel = "Stacjonarnie",
            onButtonSelected = {
                if(it == SwitchButtonType.FIRST)
                {
                    onIsOnlineSelectedChange(true)
                }
                else
                {
                    onIsOnlineSelectedChange(false)
                }
            }
        )
    }
}



@Composable
private fun TwoColumnsFiltersRow(
    label: String,
    firstFilterText: String,
    secondFilterText: String,
    onFirstFilterClick: () -> Unit,
    onSecondFilterClick: () -> Unit,
    isFirstSelected: Boolean = false,
    isSecondSelected: Boolean = false,
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
            contentAlignment = Center,
            borderColor = if(isFirstSelected)
                Orange
            else
                Green
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
            contentAlignment = Center,
            borderColor = if(isSecondSelected)
                Orange
            else
                Green
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
private fun CategoriesRow(
    categories: Map<FilterModel.Category, Boolean>,
    onCategorySelectionChange: (FilterModel.Category, Boolean) -> Unit = {_,_->}
)
{
    HeaderText(text = "Główna:")
    Spacer(modifier = Modifier.height(10.dp))
    LazyVerticalGrid(columns = GridCells.Fixed(3))
    {
        items(categories.toList())
        {
            FilterLabelBox(
                modifier = Modifier
                    .padding(3.dp)
                    .clickable { onCategorySelectionChange(it.first, !it.second) },
                text = {
                    Text(
                        modifier = Modifier.padding(end = 5.dp),
                        text = it.first.tittle,
                        fontFamily = montserratFont,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp
                    )
                },
                icon = {
                    Icon(
                        modifier = Modifier.padding(3.dp),
                        painter = painterResource(id = it.first.iconDrawableId),
                        contentDescription = ""
                    )
                },
                borderColor =
                if(it.second)
                    Orange
                else
                    Green
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun EditEventScreen2Preview() {
    EditEventScreen2()
}