package com.pawlowski.stuboard.ui.other_screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.pawlowski.stuboard.presentation.filters.FilterModel
import com.pawlowski.stuboard.ui.components.FilterLabelBox
import com.pawlowski.stuboard.ui.theme.Green
import com.pawlowski.stuboard.ui.theme.Orange
import com.pawlowski.stuboard.ui.theme.montserratFont
import com.pawlowski.stuboard.ui.utils.PreviewUtils

@Composable
fun MapFiltersDialog(
    cityFilters: Map<FilterModel.Place.RealPlace, Boolean>,
    otherSelectedFilters: Map<FilterModel, Boolean>,
    onFilterSelectionChange: (FilterModel, Boolean) -> Unit = {_, _ -> },
    onDismiss: () -> Unit = {},
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(modifier = Modifier
            .padding(horizontal = 15.dp)
            .fillMaxWidth()
            .wrapContentHeight()) {

            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Filtry dla mapy",
                    fontFamily = montserratFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Miasto (wybierz min. 1)",
                    fontFamily = montserratFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 11.sp,
                )
                Spacer(modifier = Modifier.height(10.dp))
                LazyVerticalGrid(modifier = Modifier.padding(horizontal = 10.dp).fillMaxWidth(), columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.SpaceEvenly) {
                    items(cityFilters.entries.toList()) { item ->
                        FilterLabelBox(modifier = Modifier
                            .padding(horizontal = 3.dp, vertical = 5.dp)
                            .clickable { onFilterSelectionChange(item.key, !item.value) },
                            text = {
                            Text(text = item.key.tittle)
                        }, borderColor = if(item.value)
                                Orange
                            else
                                Green,
                            contentAlignment = Alignment.Center)
                    }
                }

                Text(
                    text = "Inne wybrane filtry:",
                    fontFamily = montserratFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 11.sp,
                )

                LazyVerticalGrid(modifier = Modifier.padding(horizontal = 10.dp).fillMaxWidth(), columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.SpaceEvenly) {
                    items(otherSelectedFilters.entries.toList()) { item ->
                        FilterLabelBox(modifier = Modifier
                            .padding(horizontal = 3.dp, vertical = 5.dp)
                            .clickable { onFilterSelectionChange(item.key, !item.value) },
                            text = {
                            Text(text = item.key.tittle)
                        }, borderColor = if(item.value)
                            Orange
                        else
                            Green,
                            contentAlignment = Alignment.Center)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MapFiltersDialogPreview() {
    MapFiltersDialog(
        cityFilters = PreviewUtils.defaultFilters
            .filterIsInstance<FilterModel.Place.RealPlace>()
            .associateWith { true },
        otherSelectedFilters = PreviewUtils.defaultFilters
            .filter { it !is FilterModel.Place }
            .associateWith { true }
    )
}