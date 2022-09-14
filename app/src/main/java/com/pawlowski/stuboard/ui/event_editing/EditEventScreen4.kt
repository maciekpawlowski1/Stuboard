package com.pawlowski.stuboard.ui.event_editing

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.presentation.edit_event.Organisation
import com.pawlowski.stuboard.ui.theme.Green
import com.pawlowski.stuboard.ui.theme.LightGray
import com.pawlowski.stuboard.ui.theme.montserratFont

@Composable
fun EditEventScreen4(
    organisationSearchInput: () -> String = {""},
    onOrganisationSearchInputChange: (String) -> Unit = {},
    suggestedOrganisations: () -> List<Organisation.Existing> = { listOf() }
)
{
    Column(
        modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()) {
            IconButton(modifier = Modifier.align(Alignment.CenterStart), onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_back_icon),
                    contentDescription = ""
                )
            }
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Organizator",
                fontFamily = montserratFont,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }

        Text(
            text = "Wybierz z listy lub wpisz własną nazwę",
            fontFamily = montserratFont,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )


        val textFieldColors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Green,
            focusedLabelColor = Green,
            cursorColor = Green,
            backgroundColor = Color.White
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.8f),
            value = organisationSearchInput(),
            onValueChange = onOrganisationSearchInputChange,
            label = {
                Text(text = "Szukaj...")
            },
            singleLine = true,
            trailingIcon = {
                Icon(painter = painterResource(id = R.drawable.search_icon), contentDescription = "")
            },
            colors = textFieldColors
        )

        Spacer(modifier = Modifier.height(15.dp))

        LazyColumn(modifier = Modifier.padding(horizontal = 10.dp))
        {
            items(suggestedOrganisations()) {
                OrganisationCard(
                    modifier = Modifier.padding(vertical = 5.dp),
                    organisationTittle = it.tittle,
                    organisationImageUrl = it.imageUrl,
                    onCardClick = {

                    }
                )
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Lub własne:",
                    fontFamily = montserratFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                )
                Spacer(modifier = Modifier.height(10.dp))
                OrganisationCard(
                    modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp),
                    organisationTittle = "Organiz"
                )
            }
        }



    }
}

@Composable
private fun OrganisationCard(
    modifier: Modifier = Modifier,
    organisationImageUrl: String? = null,
    organisationTittle: String = "",
    onCardClick: () -> Unit = {}
)
{
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(55.dp)
            .clickable { onCardClick() },
        elevation = 7.dp
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(15.dp))
            Card(shape = CircleShape, modifier = Modifier.size(40.dp), backgroundColor = LightGray) {
                organisationImageUrl?.let {
                    AsyncImage(model = "", contentDescription = "")

                }?: kotlin.run {
                    Icon(
                        modifier= Modifier.padding(7.dp),
                        painter = painterResource(id = R.drawable.organisation_icon),
                        contentDescription = ""
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = organisationTittle,
                fontFamily = montserratFont,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditEventScreen4Preview()
{
    EditEventScreen4()
}