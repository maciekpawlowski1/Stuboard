package com.pawlowski.stuboard.ui.event_editing

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.dateTimePicker
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.ui.theme.Green
import com.pawlowski.stuboard.ui.theme.LighterMidGrey
import com.pawlowski.stuboard.ui.theme.MidGrey
import com.pawlowski.stuboard.ui.theme.montserratFont
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun EditEventScreen1(
    tittleInput: () -> String = { "" },
    onTittleInputChange: (String) -> Unit = {},
    sinceTime: Long? = null,
    toTime: Long? = null,
    onSinceTimeChange: (Long) -> Unit = {},
    onToTimeChange: (Long) -> Unit = {}
) {
    val isImageSelected = false
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        val screenWidth = LocalConfiguration.current.screenWidthDp

        Box {
            if (isImageSelected) {
                AsyncImage(
                    modifier = Modifier
                        .padding(bottom = 25.dp)
                        .fillMaxWidth()
                        .height((screenWidth / 1.5).dp),
                    model = "",
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds
                )
            } else {
                Surface(
                    modifier = Modifier
                        .padding(bottom = 25.dp)
                        .fillMaxWidth()
                        .height((screenWidth / 1.5).dp), color = LighterMidGrey
                ) {

                }
            }

            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 20.dp), onClick = { /*TODO*/ }, backgroundColor = Green
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.image_icon),
                    contentDescription = "",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            value = tittleInput(),
            onValueChange = onTittleInputChange,
            label = { Text(text = "Tytuł wydarzenia (max. 35 znaków)") }
        )

        Spacer(modifier = Modifier.height(30.dp))

        val context = LocalContext.current
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



            ClickToPickDateCard(
                modifier = Modifier
                    .clickable {
                        MaterialDialog(context).show {
                            dateTimePicker(show24HoursView = true, requireFutureDateTime = true) { _, datetime ->
                                onSinceTimeChange(datetime.time.time)
                            }
                        }
                    },
                timeInput = sinceTime
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
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
            ClickToPickDateCard(
                modifier = Modifier
                    .clickable {
                        MaterialDialog(context).show {
                            dateTimePicker(requireFutureDateTime = true) { _, datetime ->
                                onToTimeChange(datetime.time.time)
                            }
                        }
                    },
                timeInput = toTime
            )
        }

    }
}

@Composable
fun ClickToPickDateCard(modifier: Modifier = Modifier, timeInput: Long? = null) {
    Card(
        elevation = 10.dp, modifier = modifier
            .width(195.dp)
            .height(48.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(10.dp))
            val simpleDateFormat = remember {
                SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
            }
            val timeInputString = remember(timeInput) {
                if (timeInput != null) {
                    simpleDateFormat.format(Date(timeInput))
                } else
                    "Kliknij aby wybrać datę"
            }
            Text(
                text = timeInputString,
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

@Preview(showBackground = true)
@Composable
private fun EditEventScreen1Preview() {
    EditEventScreen1()
}