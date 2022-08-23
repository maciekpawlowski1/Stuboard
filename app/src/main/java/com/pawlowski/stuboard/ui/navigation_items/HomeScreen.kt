package com.pawlowski.stuboard.ui.navigation_items
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.pawlowski.stuboard.ui.theme.StuboardTheme

@Composable
fun HomeScreen()
{
    Surface {
        Column {
            Text(text = "Home screen")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    StuboardTheme {
        HomeScreen()
    }
}