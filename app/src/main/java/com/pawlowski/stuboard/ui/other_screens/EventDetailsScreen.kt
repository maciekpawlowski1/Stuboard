package com.pawlowski.stuboard.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.ui.models.EventItemWithDetails
import com.pawlowski.stuboard.ui.models.OrganisationItemForPreview
import com.pawlowski.stuboard.ui.theme.Green
import com.pawlowski.stuboard.ui.theme.LightGreen
import com.pawlowski.stuboard.ui.theme.jostFontNormalWeight
import com.pawlowski.stuboard.ui.theme.montserratFont
import com.pawlowski.stuboard.ui.utils.PreviewUtils
import com.pawlowski.stuboard.ui.utils.VerticalDivider

@Composable
fun EventDetailsScreen(eventId: Int)
{
    //TODO: Change to collecting from ViewModel
    val eventItemWithDetails: EventItemWithDetails = PreviewUtils.defaultFullEvent

    val screenWidth = LocalConfiguration.current.screenWidthDp
    Surface(modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth()) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

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

            DateRow(
                date = eventItemWithDetails.dateDisplay,
                hour = eventItemWithDetails.hourDisplay
            )

            Divider()

            PlaceRow(place = eventItemWithDetails.place)

            Divider()

            OrganisationRow(organisation = eventItemWithDetails.organisation)

            Divider()

            CategoriesRow(categoriesDrawableIds = PreviewUtils.defaultFullEvent.categoriesDrawablesId)

            Divider()

            PriceRow(price = PreviewUtils.defaultFullEvent.price)

            Divider()

            DescriptionRow(description = PreviewUtils.defaultFullEvent.description)

            Divider()

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(70.dp))
        }
    }
}

@Composable
private fun DescriptionRow(description: String, linesUntilOverflow: Int = 8)
{
    Column(modifier = Modifier.padding(10.dp)) {
        var showMore by remember {
            mutableStateOf(false)
        }
        Text(
            text = "Opis:",
            fontFamily = montserratFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
        Text(
            modifier= Modifier
                .padding(top = 10.dp)
                .animateContentSize(tween(300)),
            text = description,
            maxLines = if(showMore) Int.MAX_VALUE else linesUntilOverflow,
            overflow = if(showMore) TextOverflow.Clip else TextOverflow.Ellipsis,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            fontFamily = jostFontNormalWeight
                )
        Text(
            modifier = Modifier
                .align(End)
                .clickable { showMore = !showMore }
                .padding(vertical = 5.dp),
            text = if(showMore) "Pokaż mniej" else "Pokaż więcej",
            textDecoration = TextDecoration.Underline,
            color = LightGreen,
            fontFamily = montserratFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun PriceRow(price: Float)
{
    val priceText = if(price == 0f)
    {
        "Udział jest darmowy"
    }
    else
        "Udział kosztuje $price zł"
    TableRow(header = "Cena:") {
        Text(
            modifier = Modifier
                .padding(horizontal = 10.dp),
            text = priceText,
            fontFamily = montserratFont,
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
private fun CategoriesRow(categoriesDrawableIds: List<Int>)
{
    TableRow(header = "Kategorie:") {
        Row {
            categoriesDrawableIds.forEach {
                CategoryRoundIcon(modifier = Modifier.padding(horizontal = 5.dp), categoryDrawableId = it)
            }
        }
    }
}

@Composable
fun CategoryRoundIcon(modifier: Modifier = Modifier, categoryDrawableId: Int, size: Dp = 42.dp)
{
    Card(modifier = modifier
        .size(size),
        shape = CircleShape,
        backgroundColor = Green
    ) {
        Icon(
            modifier = Modifier.padding(6.dp),
            painter = painterResource(id = categoryDrawableId),
            contentDescription = "",
            tint = Color.White
        )
    }
}

@Composable
private fun PlaceRow(place: String)
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
private fun OrganisationRow(organisation: OrganisationItemForPreview, onOrganisationClick: () -> Unit = {})
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
private fun DateRow(date: String, hour: String)
{
    Row(modifier = Modifier.height(57.dp), verticalAlignment = CenterVertically) {
        Icon(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxHeight(0.6f),
            painter = painterResource(id = R.drawable.calendar_icon),
            contentDescription = ""
        )
        Column {
            Text(text = date,
                fontFamily = montserratFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = Green
            )
            Text(
                text = hour,
                fontFamily = montserratFont,
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp,
            )
        }
    }
}

@Composable
private fun TableRow(header: String, height: Dp = 57.dp,secondColumnContent: @Composable () -> Unit)
{
    Row(modifier = Modifier.height(height)) {
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
private fun TableFirstColumn(textComposable: @Composable () -> Unit)
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
private fun TableSecondColumn(content: @Composable () -> Unit)
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
private fun FirstColumnHeaderText(text: String)
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



@Preview(showBackground = true)
@Composable
private fun EventDetailsScreenPreview()
{
    EventDetailsScreen(-1)
}