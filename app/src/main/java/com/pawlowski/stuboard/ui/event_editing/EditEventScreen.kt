package com.pawlowski.stuboard.ui.event_editing

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pawlowski.stuboard.ui.register_screen.swappingTransitionSpec
import com.pawlowski.stuboard.ui.theme.Green
import com.pawlowski.stuboard.ui.theme.montserratFont


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun EditEventScreen() {
    var currentScreen by remember {
        mutableStateOf(EditEventScreenType.FIRST)
    }
    Column {
        AnimatedContent(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            targetState = currentScreen,
            transitionSpec = {
                swappingTransitionSpec()
                { prev, new ->
                    prev.num < new.num
                }
            }
        ) { screen ->
            when(screen) {
                EditEventScreenType.FIRST -> {
                    EditEventScreen1()
                }
                EditEventScreenType.SECOND -> {
                    EditEventScreen2()
                }
                else -> {

                }
            }
        }

        NavigationBox(currentScreen.num, 5)
    }

}

@Composable
private fun NavigationBox(currentScreenNum: Int, allScreensCount: Int)
{
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp),
    elevation = 7.dp)
    {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center)
        {
            Text(
                text = "$currentScreenNum z $allScreensCount",
                fontFamily = montserratFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
            )
            TextButton(
                modifier = Modifier
                    .align(Alignment.CenterEnd),
                onClick = { /*TODO*/ },
            ) {
                Text(text = "Przejdź dalej", color = Green)
            }

        }
    }
}

enum class EditEventScreenType(val num: Int) {
    FIRST(1),
    SECOND(2),
    THIRD(3)
}

@Preview(showBackground = true)
@Composable
private fun EditEventScreenPreview()
{
    EditEventScreen()
}