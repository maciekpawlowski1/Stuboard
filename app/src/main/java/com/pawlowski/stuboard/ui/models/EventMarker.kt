package com.pawlowski.stuboard.ui.models

import com.google.android.gms.maps.model.LatLng

data class EventMarker(
    val position: LatLng,
    val iconId: Int,
    val eventTittle: String
)
