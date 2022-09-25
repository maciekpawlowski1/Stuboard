package com.pawlowski.stuboard.ui.event_editing

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.ui.models.EventMarker
import com.pawlowski.stuboard.ui.screens_in_bottom_navigation_related.MyGoogleMap
import com.pawlowski.stuboard.ui.theme.Green
import com.pawlowski.stuboard.ui.theme.LighterMidGrey
import com.pawlowski.stuboard.ui.theme.montserratFont
import kotlin.Exception

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditEventScreen3(
    city: () -> String = {""},
    streetAndNum: () -> String = {""},
    country: () -> String = {""},
    placeName: () -> String = {""},
    onCityChange: (String) -> Unit = {},
    onStreetChange: (String) -> Unit = {},
    onCountryChange: (String) -> Unit = {},
    onPlaceNameChange: (String) -> Unit = {},
    positionOnMap: () -> LatLng? = { null },
    markerRes: () -> Int? = { null }
)
{
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        Box {
            MapPreview(
                modifier = Modifier
                    .padding(bottom = 25.dp),
                position = positionOnMap(),
                markerRes = markerRes()
            )
            val context = LocalContext.current
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 20.dp), onClick = {
                    //TODO: open location picker dialog
                     Toast.makeText(context, "Opcja wyszukiwania na mapie będzie dostępna wkrótce!", Toast.LENGTH_SHORT).show()
                }, backgroundColor = Green
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.map_icon),
                    contentDescription = "",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            modifier = Modifier
                .align(CenterHorizontally),
            text = "Podaj adres lub wybierz na mapie:",
            fontFamily = montserratFont,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(10.dp))


        val textFieldColors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Green,
            focusedLabelColor = Green,
            cursorColor = Green,
            backgroundColor = Color.White
        )
        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current
        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            value = city(),
            singleLine = true,
            onValueChange = { onCityChange(it) },
            colors = textFieldColors,
            label = { Text(text = "Miasto") },
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            value = streetAndNum(),
            singleLine = true,
            onValueChange = { onStreetChange(it) },
            colors = textFieldColors,
            label = { Text(text = "Ulica i nr budynku") },
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            value = country(),
            singleLine = true,
            onValueChange = { onCountryChange(it) },
            colors = textFieldColors,
            label = { Text(text = "Kraj") },
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            value = placeName(),
            singleLine = true,
            onValueChange = { onPlaceNameChange(it) },
            colors = textFieldColors,
            label = { Text(text = "Nazwa miejsca (opcjonalnie)") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
        )
        Spacer(modifier = Modifier.height(10.dp))


    }
}

@Composable
private fun MapPreview(modifier: Modifier = Modifier, position: LatLng? = null, markerRes: Int? = null)
{
    val screenWidth = LocalConfiguration.current.screenWidthDp
    position?.let {
        val cameraPositionState = rememberCameraPositionState()
        {
            this.position = CameraPosition.fromLatLngZoom(it, 10f)
        }
        MyGoogleMap(
            modifier= modifier
                .fillMaxWidth()
                .height((screenWidth / 1.5).dp),
            markers = listOf(
                EventMarker(it, markerRes?:R.drawable.naukowe_marker_icon, "", "-1")
            ),
            disableAllGestures = true,
            moveCameraToMarkersBound = false,
            cameraPositionState = cameraPositionState
        )


        LaunchedEffect(it)
        {
            try {
                cameraPositionState.move(CameraUpdateFactory.newLatLng(it))
            }
            catch (e: Exception) {}
        }

    }?: run {
        Surface(modifier = modifier
            .fillMaxWidth()
            .height((screenWidth / 1.5).dp),
            color = LighterMidGrey
        ) {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditEventScreen3Preview()
{
    EditEventScreen3()
}