package com.pawlowski.stuboard.ui.event_editing

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.ui.theme.Green
import com.pawlowski.stuboard.ui.theme.LighterMidGrey
import com.pawlowski.stuboard.ui.theme.MidGrey
import com.pawlowski.stuboard.ui.theme.montserratFont

@Composable
fun EditEventScreen1()
{
    val isImageSelected = false
    Column(modifier = Modifier.fillMaxSize()) {
        val screenWidth = LocalConfiguration.current.screenWidthDp

        Box {
            if(isImageSelected)
            {
                AsyncImage(modifier = Modifier
                    .padding(bottom = 25.dp)
                    .fillMaxWidth()
                    .height((screenWidth / 1.5).dp),
                    model = "",
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds
                )
            }
            else
            {
                Surface(
                    modifier = Modifier
                        .padding(bottom = 25.dp)
                        .fillMaxWidth()
                        .height((screenWidth / 1.5).dp), color = LighterMidGrey
                ) {

                }
            }

            FloatingActionButton(modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp), onClick = { /*TODO*/ }, backgroundColor = Green
            ) {
                Icon(painter = painterResource(id = R.drawable.image_icon), contentDescription = "", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
            value = "",
            onValueChange = {},
            label = { Text(text = "Tytuł wydarzenia (max. 35 znaków)") }
        )

        Spacer(modifier = Modifier.height(30.dp))


        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Od:",
                fontFamily = montserratFont,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.width(10.dp))
            ClickToPickDateCard()
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Do:",
                fontFamily = montserratFont,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.width(10.dp))
            ClickToPickDateCard()
        }

    }
}

@Composable
fun ClickToPickDateCard() {
    Card(elevation = 10.dp, modifier = Modifier
        .width(195.dp)
        .height(48.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Kliknij aby wybrać datę",
                fontFamily = montserratFont,
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp
            )
            Spacer(modifier = Modifier.width(10.dp))
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
                Icon(
                    modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, end = 10.dp),
                    painter = painterResource(id = R.drawable.calendar_icon),
                    contentDescription = "",
                    tint = MidGrey
                )
            }

        }
    }
}