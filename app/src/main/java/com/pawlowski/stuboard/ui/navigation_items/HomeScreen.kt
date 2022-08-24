package com.pawlowski.stuboard.ui.navigation_items
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.ui.models.EventItemForPreview
import com.pawlowski.stuboard.ui.theme.*
import com.pawlowski.stuboard.ui.utils.PreviewUtils

@Composable
fun HomeScreen(navController: NavController?, preview: Boolean = false)
{
    Surface {
        Column {
            Map(preview)
            Surface(modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()) {
                Column {
                    SearchCardButton(15.dp)
                    {
                        navController?.navigate(BottomNavItems.Search.route)
                        {
                            navController.graph.startDestinationRoute?.let { screen_route ->
                                popUpTo(screen_route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }

                    CategoriesRow()

                    LabelsRow(padding = PaddingValues(vertical = 10.dp, horizontal = 5.dp), label1 = "Najwcześniej", label2 = "Więcej") {

                    }

                    EventsRow(eventItemsForPreview = PreviewUtils.defaultEventPreviews)

                    LabelsRow(padding = PaddingValues(vertical = 10.dp, horizontal = 5.dp), label1 = "Online", label2 = "Więcej") {

                    }

                    EventsRow(eventItemsForPreview = PreviewUtils.defaultEventPreviews.filter { it.place.lowercase() == "online" })
                }
            }
        }
    }

}

@Composable
fun SearchCardButton(horizontalPadding: Dp, onClick: () -> Unit)
{


    Card(elevation = 8.dp,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(0.3.dp, MidGrey),
        backgroundColor = LightGray,
        modifier = Modifier
            .padding(
                horizontal = horizontalPadding,
                vertical = 20.dp
            )
            .fillMaxWidth()
            .height(55.dp)
            .clickable {
                onClick.invoke()
            }

    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(modifier = Modifier.padding(horizontal = 15.dp), painter = painterResource(id = R.drawable.search_icon), contentDescription = "")
            Text(text = stringResource(id = R.string.search_for_events), fontWeight = FontWeight.Normal, color = LighterMidGrey, style = MaterialTheme.typography.h5)
        }
    }
}

@Composable
fun CategoriesRow()
{
    data class CategoryItem(val imageId: Int, val tittle: String)
    val categoryItems = listOf(
        CategoryItem(R.drawable.concerts_category_image, "Koncerty"),
        CategoryItem(R.drawable.learning_category_image, "Naukowe"),
        CategoryItem(R.drawable.spors_category_image, "Sportowe"),
    )
    LazyRow()
    {
        items(categoryItems)
        {
            CategoryCard(imageId = it.imageId, tittle = it.tittle, PaddingValues(start = 10.dp))
            {

            }
        }

    }
}

@Composable
fun LabelsRow(padding: PaddingValues, label1: String, label2: String, onLabel2Click: () -> Unit)
{
    Row(modifier = Modifier.padding(padding)) {
        Box(modifier = Modifier.weight(1f)) {
            Text(text = label1, fontWeight = FontWeight.Medium, modifier = Modifier.padding(5.dp))
        }
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
            Text(
                text = label2, modifier = Modifier
                    .clickable {
                        onLabel2Click.invoke()
                    }
                    .padding(5.dp),
                fontWeight = FontWeight.Medium,
                textDecoration = TextDecoration.Underline,
                color = Green
                )
        }
    }
}

@Composable
fun CategoryCard(imageId: Int, tittle: String, padding: PaddingValues, onCardClick: () -> Unit)
{
    Card(shape = RoundedCornerShape(8.dp), modifier = Modifier
        .padding(padding)
        .width(115.dp)
        .height(75.dp)
        .clickable {
            onCardClick.invoke()
        }
    ) {
        Box {
            Image(painter = painterResource(id = imageId), contentScale = ContentScale.FillBounds, contentDescription = "")
            Text(text = tittle, color = Color.White,
                modifier = Modifier.align(Alignment.BottomCenter),
                fontWeight = FontWeight.SemiBold
            )
        }

    }
}

@Composable
fun Map(preview: Boolean = false)
{
    if(!preview)
    {
        GoogleMap(modifier = Modifier
            .fillMaxWidth()
            .height(170.dp),
            cameraPositionState = rememberCameraPositionState()
        )
    }
    else
    {
        Surface(modifier = Modifier
            .fillMaxWidth()
            .height(170.dp),
            color = Color.Gray
        ) {

        }
    }

}

@Composable
fun EventCard(eventItemForPreview: EventItemForPreview, padding: PaddingValues, onCardClick: () -> Unit)
{
    Card(
        modifier = Modifier
            .padding(padding)
            .width(166.dp)
            .height(177.dp)
            .clickable { onCardClick.invoke() }
        ,
        elevation = 5.dp
    ) {
        Column(modifier = Modifier.padding(top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                modifier = Modifier
                    .padding(bottom = 7.dp)
                    .width(150.dp)
                    .height(100.dp),
                model = eventItemForPreview.imageUrl,
                contentDescription = "",
                contentScale = ContentScale.FillBounds
            )
            
            Text(text = eventItemForPreview.tittle,
                modifier= Modifier.padding(horizontal = 5.dp),
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )
            
            Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxHeight().padding(bottom = 5.dp)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = eventItemForPreview.place,
                        modifier= Modifier.padding(start = 5.dp, end = 5.dp),
                        fontWeight = FontWeight.Light,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center)
                    Text(text = eventItemForPreview.dateDisplayString,
                        modifier= Modifier.padding(start = 5.dp, end = 5.dp),
                        fontWeight = FontWeight.Normal,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center)
                }
            }

        }
    }
}

@Composable
fun EventsRow(eventItemsForPreview: List<EventItemForPreview>)
{
    LazyRow()
    {
        items(eventItemsForPreview)
        {
            EventCard(eventItemForPreview = it, PaddingValues(start = 10.dp))
            {

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    StuboardTheme {
        HomeScreen(navController = null, preview = true)
    }
}