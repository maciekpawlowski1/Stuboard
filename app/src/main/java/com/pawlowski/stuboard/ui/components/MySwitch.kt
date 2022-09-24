package com.pawlowski.stuboard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pawlowski.stuboard.ui.theme.Green
import com.pawlowski.stuboard.ui.theme.MidGrey
import com.pawlowski.stuboard.ui.theme.montserratFont

enum class SwitchButtonType {
    FIRST,
    SECOND
}

@Composable
fun MySwitch(
    modifier: Modifier = Modifier,
    selectedButton: SwitchButtonType,
    firstButtonLabel: String,
    secondButtonLabel: String,
    onButtonSelected: (SwitchButtonType) -> Unit = {}
) {
    Card(
        modifier = modifier
            .height(36.dp)
            .fillMaxWidth()
    ) {
        Row {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(
                        if (selectedButton == SwitchButtonType.FIRST)
                            Green
                        else
                            Color.White
                    )
                    .clickable { onButtonSelected.invoke(SwitchButtonType.FIRST) },
                contentAlignment = Alignment.Center
            )
            {
                if (selectedButton == SwitchButtonType.FIRST) {
                    SelectedText(text = firstButtonLabel)
                } else {
                    NotSelectedText(text = firstButtonLabel)
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(
                        if (selectedButton == SwitchButtonType.FIRST)
                            Color.White
                        else
                            Green
                    )
                    .clickable { onButtonSelected.invoke(SwitchButtonType.SECOND) },
                contentAlignment = Alignment.Center
            )
            {
                if (selectedButton == SwitchButtonType.SECOND) {
                    SelectedText(text = secondButtonLabel)
                } else {
                    NotSelectedText(text = secondButtonLabel)
                }
            }
        }
    }
}

@Composable
private fun SelectedText(text: String) {
    Text(
        text = text,
        color = Color.White,
        fontFamily = montserratFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
    )
}

@Composable
private fun NotSelectedText(text: String) {
    Text(
        text = text,
        color = MidGrey,
        fontFamily = montserratFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    )
}