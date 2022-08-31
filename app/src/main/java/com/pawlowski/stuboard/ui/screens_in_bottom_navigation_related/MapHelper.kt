package com.pawlowski.stuboard.ui.screens_in_bottom_navigation_related

import android.Manifest
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*
import com.pawlowski.stuboard.ui.models.EventMarker

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MyGoogleMap(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    preview: Boolean = false,
    markers: List<EventMarker>,
    locationButtonsEnabledWithAskingPermission: Boolean = false,
    moveCameraToMarkersBound: Boolean = false,
    zoomButtonsEnabled: Boolean = true,
    disableAllGestures: Boolean = false,
    onMarkerClick: (EventMarker) -> Unit = {},
    onMapClick: () -> Unit = {}
)
{
    if(!preview)
    {
        val showLocationButtons = if(locationButtonsEnabledWithAskingPermission)
        {
            val locationPermissionState = rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)
            val hasPermission = locationPermissionState.status.isGranted

            if(!hasPermission && locationPermissionState.status !is PermissionStatus.Denied)
            {
                val lifecycleOwner = LocalLifecycleOwner.current
                DisposableEffect(key1 = lifecycleOwner, effect = {
                    val eventObserver = LifecycleEventObserver { _, event ->
                        when (event) {
                            Lifecycle.Event.ON_START -> {
                                locationPermissionState.launchPermissionRequest()
                            }
                            else -> {}
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(eventObserver)

                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(eventObserver)
                    }
                })
            }
            hasPermission
        }
        else
            false







        val mapProperties by remember(showLocationButtons) { mutableStateOf(MapProperties(isMyLocationEnabled = showLocationButtons)) }
        val uiSettings by remember(showLocationButtons) { mutableStateOf(MapUiSettings(myLocationButtonEnabled = showLocationButtons, zoomControlsEnabled = zoomButtonsEnabled, scrollGesturesEnabled = !disableAllGestures, zoomGesturesEnabled = !disableAllGestures, rotationGesturesEnabled = !disableAllGestures)) }
        GoogleMap(
            properties = mapProperties,
            uiSettings = uiSettings,
            modifier = modifier,
            cameraPositionState = cameraPositionState,
            onMapClick = { onMapClick.invoke() } //TODO: check doesn't it ignore Marker onClick
        )
        {
            val context = LocalContext.current
            markers.forEach {
                CustomMapMarker(
                    context = context,
                    position = it.position,
                    title = it.eventTittle,
                    iconResourceId = it.iconId
                )
                {
                    onMarkerClick.invoke(it)
                    true
                }
            }

        }



        val markersPositions = remember(markers) {
            markers.map { it.position }
        }
        //TODO: Check why sometimes it completes, sometimes stops
        LaunchedEffect(key1 = moveCameraToMarkersBound, key2= markersPositions) {
            if(moveCameraToMarkersBound && markers.isNotEmpty()){
                val builder = LatLngBounds.Builder()
                markers.forEach {
                    builder.include(it.position)
                }
                //Animate to marker bounds
                cameraPositionState.move(CameraUpdateFactory.newLatLngBounds(builder.build(), 150))
            }



        }
    }
    else
    {
        Surface(modifier = modifier,
            color = Color.Gray
        ) {

        }
    }

}