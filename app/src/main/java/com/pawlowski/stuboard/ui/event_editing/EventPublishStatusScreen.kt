package com.pawlowski.stuboard.ui.event_editing

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.ui.models.EventItemForPreview
import com.pawlowski.stuboard.ui.screens_in_bottom_navigation_related.screens.EventCard
import com.pawlowski.stuboard.ui.theme.Green
import com.pawlowski.stuboard.ui.theme.Orange
import com.pawlowski.stuboard.ui.theme.montserratFont

@Composable
fun EventPublishStatusScreen(
    onNavigateBack: () -> Unit = {}
)
{
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(5.dp))
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart)
        {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(painter = painterResource(id = R.drawable.arrow_back2_icon), contentDescription = "")
            }
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Status publikowania",
                fontFamily = montserratFont,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        StatusCard(
            modifier = Modifier
                .padding(horizontal = 15.dp),
            statusColor = Orange,
            statusText = "Wydarzenie jest w trakcje edycji",
            showPublishButton = true
        )

        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "Podgląd wydarzenia:",
            fontFamily = montserratFont,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(15.dp))
        EventCard(eventItemForPreview = EventItemForPreview())
    }
}

@Composable
fun StatusCard(modifier: Modifier = Modifier, statusColor: Color, statusText: String, showPublishButton: Boolean)
{
    Card(modifier = modifier
        .fillMaxWidth()
        .height(105.dp), shape = RectangleShape, elevation = 7.dp) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center)
        {
            Row(modifier = Modifier.fillMaxWidth(0.9f), verticalAlignment = Alignment.CenterVertically) {
                Card(shape = CircleShape, modifier = Modifier.size(50.dp), backgroundColor = statusColor) {

                }
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    text = statusText,
                    fontFamily = montserratFont,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp
                )
            }

            if(showPublishButton)
            {
                TextButton(modifier = Modifier.align(Alignment.BottomEnd),onClick = { /*TODO*/ }) {
                    Text(text = "Opublikuj", color = Green)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventPublishStatusScreenPreview()
{
    EventPublishStatusScreen()
}