package com.pawlowski.stuboard.ui.screens_in_bottom_navigation_related.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.pawlowski.stuboard.ui.theme.StuboardTheme

@Composable
fun SearchScreen()
{
    Surface {
        Column {
            Text(text = "Search screen")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    StuboardTheme {
        SearchScreen()
    }
}