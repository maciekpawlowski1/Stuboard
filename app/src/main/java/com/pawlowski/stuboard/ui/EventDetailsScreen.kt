package com.pawlowski.stuboard.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.ui.models.EventItemWithDetails
import com.pawlowski.stuboard.ui.models.OrganisationItemForPreview
import com.pawlowski.stuboard.ui.theme.Green
import com.pawlowski.stuboard.ui.theme.montserratFont
import com.pawlowski.stuboard.ui.utils.PreviewUtils

@Composable
fun EventDetailsScreen(eventItemWithDetails: EventItemWithDetails = PreviewUtils.defaultFullEvent)
{
    val screenWidth = LocalConfiguration.current.screenWidthDp
    Surface(modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth()) {
        Column {

            //Screen images will be in 1.5:1 proportion (width:height)
            AsyncImage(modifier = Modifier
                .fillMaxWidth()
                .height((screenWidth / 1.5).dp),
                model = eventItemWithDetails.imageUrl,
                contentDescription = "",
                contentScale = ContentScale.FillBounds
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 15.dp)
                    .align(CenterHorizontally),
                text = eventItemWithDetails.tittle,
                fontFamily = montserratFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Divider()

            PlaceRow(place = eventItemWithDetails.place)

            Divider()

            OrganisationRow(organisation = eventItemWithDetails.organisation)

            Divider()
        }
    }
}

@Composable
fun PlaceRow(place: String)
{
    TableRow(header = "Miejsce:") {
        Text(
            modifier = Modifier.padding(horizontal = 10.dp),
            text = place,
            fontFamily = montserratFont,
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp
        )
    }

}

@Composable
fun OrganisationRow(organisation: OrganisationItemForPreview, onOrganisationClick: () -> Unit = {})
{
    TableRow(header = "Organizator:") {
        Row(verticalAlignment = CenterVertically) {
            Card(shape = CircleShape, modifier = Modifier
                .padding(5.dp)
                .height(42.dp)
                .width(42.dp)) {
                AsyncImage(model = organisation.logoImageUrl, contentDescription = "", contentScale = ContentScale.Fit)
            }
            Text(
                modifier= Modifier
                    .padding(end = 4.dp)
                    .weight(1f),
                text = organisation.tittle,
                fontFamily = montserratFont,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
            Box(modifier = Modifier.clickable { onOrganisationClick.invoke() })
            {
                Icon(painter = painterResource(id = R.drawable.arrow_right_icon), contentDescription = "", tint = Green)
            }
        }

    }
}

@Composable
fun TableRow(header: String, height: Dp = 57.dp,secondColumnContent: @Composable () -> Unit)
{
    Row(modifier = Modifier.height(57.dp)) {
        TableFirstColumn {
            FirstColumnHeaderText(text = header)
        }
        VerticalDivider()
        TableSecondColumn {
            secondColumnContent()
        }
    }
}

@Composable
fun TableFirstColumn(textComposable: @Composable () -> Unit)
{
    Box(modifier = Modifier
        .width(100.dp)
        .fillMaxHeight(),
    contentAlignment = CenterStart,
    )
    {
        textComposable()
    }
}

@Composable
fun TableSecondColumn(content: @Composable () -> Unit)
{
    Box(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(),
        contentAlignment = CenterStart,
    )
    {
        content()
    }
}

@Composable
fun FirstColumnHeaderText(text: String)
{
    Text(
        modifier = Modifier.padding(
            start=7.dp,
        ),
        text = text,
        fontFamily = montserratFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp
    )
}

@Composable
fun VerticalDivider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
    thickness: Dp = 1.dp
) {
    Box(
        modifier
            .fillMaxHeight()
            .width(thickness)
            .background(color = color)
    )
}

@Preview(showBackground = true)
@Composable
fun EventDetailsScreenPreview()
{
    EventDetailsScreen()
}