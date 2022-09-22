package com.pawlowski.stuboard.ui.other_screens

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.presentation.event_details.EventDetailsUiState
import com.pawlowski.stuboard.presentation.event_details.EventDetailsViewModel
import com.pawlowski.stuboard.presentation.event_details.IEventDetailsViewModel
import com.pawlowski.stuboard.ui.models.EventItemWithDetails
import com.pawlowski.stuboard.ui.models.OrganisationItemForPreview
import com.pawlowski.stuboard.ui.theme.Green
import com.pawlowski.stuboard.ui.theme.LightGreen
import com.pawlowski.stuboard.ui.theme.jostFontNormalWeight
import com.pawlowski.stuboard.ui.theme.montserratFont
import com.pawlowski.stuboard.ui.utils.PreviewUtils
import com.pawlowski.stuboard.ui.utils.VerticalDivider
import com.pawlowski.stuboard.ui.utils.myLoadingEffect
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Composable
fun EventDetailsScreen(viewModel: IEventDetailsViewModel = hiltViewModel<EventDetailsViewModel>())
{
    val uiState = viewModel.uiState.collectAsState()
    val displayLoadingEffectState = derivedStateOf {
        uiState.value.eventDetails == null
    }
    //TODO: Create swipe refresh
    val displaySwipeRefreshState = derivedStateOf {
        uiState.value.isRefreshing
    }
    val eventItemWithDetails: EventItemWithDetails = uiState.value.eventDetails?: EventItemWithDetails()

    val screenWidth = LocalConfiguration.current.screenWidthDp
    Surface(modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth()) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

            //Screen images will be in 1.5:1 proportion (width:height)
            AsyncImage(modifier = Modifier
                .fillMaxWidth()
                .height((screenWidth / 1.5).dp)
                .myLoadingEffect(displayLoadingEffectState.value),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(eventItemWithDetails.imageUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.image_placeholder),
                error = painterResource(id = R.drawable.image_placeholder),
                contentDescription = "",
                contentScale = ContentScale.FillBounds
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 15.dp)
                    .fillMaxWidth()
                    .align(CenterHorizontally)
                    .myLoadingEffect(displayLoadingEffectState.value),
                text = eventItemWithDetails.tittle,
                fontFamily = montserratFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Divider()

            DateRow(
                date = eventItemWithDetails.dateDisplay,
                hour = eventItemWithDetails.hourDisplay,
                isLoading = displayLoadingEffectState.value
            )

            Divider()

            PlaceRow(place = eventItemWithDetails.place, isLoading = displayLoadingEffectState.value)

            Divider()

            if(eventItemWithDetails.organisation.tittle.isNotEmpty())
            {
                OrganisationRow(organisation = eventItemWithDetails.organisation, isLoading = displayLoadingEffectState.value)

                Divider()
            }

            if(eventItemWithDetails.categoriesDrawablesId.isNotEmpty() || displayLoadingEffectState.value)
            {
                CategoriesRow(categoriesDrawableIds = eventItemWithDetails.categoriesDrawablesId, isLoading = displayLoadingEffectState.value)
                Divider()
            }


            val isFree = eventItemWithDetails.isFree
            if(isFree != null || displayLoadingEffectState.value)
            {
                PriceRow(isFree = isFree?:false, isLoading = displayLoadingEffectState.value)

                Divider()
            }



            DescriptionRow(description = eventItemWithDetails.description, isLoading = displayLoadingEffectState.value)

            Divider()

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(70.dp))
        }
    }
}

@Composable
private fun DescriptionRow(description: String, linesUntilOverflow: Int = 8, isLoading: Boolean = false)
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
                .fillMaxWidth()
                .heightIn(
                    min = if (isLoading)
                        80.dp
                    else
                        Dp.Unspecified
                )
                .myLoadingEffect(isLoading)
                .animateContentSize(tween(300)),
            text = description,
            maxLines = if(showMore) Int.MAX_VALUE else linesUntilOverflow,
            overflow = if(showMore) TextOverflow.Clip else TextOverflow.Ellipsis,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            fontFamily = jostFontNormalWeight
                )

        if(!isLoading)
        {
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
}

@Composable
private fun PriceRow(isFree: Boolean, isLoading: Boolean = false)
{
    val priceText = if(isFree)
    {
        "Udział jest darmowy"
    }
    else
        "Udział jest płatny"
    TableRow(header = "Koszt:") {
        Text(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
                .myLoadingEffect(isLoading),
            text = priceText,
            fontFamily = montserratFont,
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
private fun CategoriesRow(categoriesDrawableIds: List<Int>, isLoading: Boolean = false)
{
    TableRow(header = "Kategorie:") {
        Row {
            if(!isLoading)
            {
                categoriesDrawableIds.forEach {
                    CategoryRoundIcon(modifier = Modifier.padding(horizontal = 5.dp), categoryDrawableId = it)
                }
            }
            else
            {
                repeat(3)
                {
                    CategoryRoundIcon(
                        isLoading = isLoading,
                        modifier = Modifier.padding(horizontal = 5.dp),
                        //To not crash by case
                        categoryDrawableId = R.drawable.sports_category_icon)
                }
            }

        }
    }
}

@Composable
fun CategoryRoundIcon(modifier: Modifier = Modifier, categoryDrawableId: Int, size: Dp = 42.dp, isLoading: Boolean = false)
{
    Card(modifier = modifier
        .size(size)
        .myLoadingEffect(isLoading),
        shape = CircleShape,
        backgroundColor = Green
    ) {
        if(!isLoading)
        {
            Icon(
                modifier = Modifier.padding(6.dp),
                painter = painterResource(id = categoryDrawableId),
                contentDescription = "",
                tint = Color.White
            )
        }

    }
}

@Composable
private fun PlaceRow(place: String, isLoading: Boolean = false)
{
    TableRow(header = "Miejsce:") {
        Text(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
                .myLoadingEffect(isLoading),
            text = place,
            fontFamily = montserratFont,
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp
        )
    }

}

@Composable
private fun OrganisationRow(organisation: OrganisationItemForPreview, isLoading: Boolean = false, onOrganisationClick: () -> Unit = {})
{
    TableRow(header = "Organizator:") {
        Row(verticalAlignment = CenterVertically) {
            if(organisation.logoImageUrl.isNotEmpty())
            {
                Card(shape = CircleShape, modifier = Modifier
                    .padding(5.dp)
                    .height(42.dp)
                    .width(42.dp)
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .myLoadingEffect(isLoading),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(organisation.logoImageUrl)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.image_placeholder),
                        error = painterResource(id = R.drawable.image_placeholder),
                        contentDescription = "",

                        contentScale = ContentScale.FillBounds
                    )
                }
            }
            Text(
                modifier= Modifier
                    .padding(end = 4.dp)
                    .weight(1f)
                    .myLoadingEffect(isLoading),
                text = organisation.tittle,
                fontFamily = montserratFont,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
            if(!isLoading)
            {
                Box(modifier = Modifier.clickable { onOrganisationClick.invoke() })
                {
                    Icon(painter = painterResource(id = R.drawable.arrow_right_icon), contentDescription = "", tint = Green)
                }
            }

        }

    }
}

@Composable
private fun DateRow(modifier: Modifier = Modifier, date: String, hour: String, isLoading: Boolean)
{
    Row(modifier = modifier.height(57.dp), verticalAlignment = CenterVertically) {
        Icon(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxHeight(0.6f),
            painter = painterResource(id = R.drawable.calendar_icon),
            contentDescription = ""
        )
        Column(modifier = Modifier
            .padding(end = 10.dp)
            .weight(1f)
            .myLoadingEffect(isLoading)) {
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
    EventDetailsScreen(viewModel = object : IEventDetailsViewModel
    {
        //Mock uiState for preview
        override val uiState: StateFlow<EventDetailsUiState> =
            MutableStateFlow(EventDetailsUiState(isRefreshing = false,
            eventDetails = PreviewUtils.defaultFullEvent)).asStateFlow()
    })
}