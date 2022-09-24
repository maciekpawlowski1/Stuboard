package com.pawlowski.stuboard.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pawlowski.stuboard.ui.theme.Green

@Composable
fun FilterLabelBox(
    modifier: Modifier = Modifier,
    text: @Composable () -> Unit = { },
    borderColor: Color = Green,
    borderWith: Dp = 1.3.dp,
    height: Dp = 29.dp,
    contentAlignment: Alignment = Alignment.CenterStart,
    icon: @Composable () -> Unit = { },
) {
    Card(
        modifier = modifier
            .height(height),
        shape = RectangleShape,
        border = BorderStroke(borderWith, color = borderColor)
    ) {
        Box(contentAlignment = contentAlignment)
        {
            Row(verticalAlignment = Alignment.CenterVertically) {
                icon.invoke()
                text.invoke()
            }
        }
    }
}