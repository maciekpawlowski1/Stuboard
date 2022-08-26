package com.pawlowski.stuboard.ui.models

import com.google.android.gms.maps.model.LatLng

data class EventItemForMapScreen(
    val eventId: Int,
    val tittle: String,
    val place: String,
    val dateDisplayString: String,
    val isFree: Boolean,
    val imageUrl: String,
    val position: LatLng,
    val mainCategoryDrawableId: Int,
    val mainCategoryDrawableIdWhenSelected: Int
)
