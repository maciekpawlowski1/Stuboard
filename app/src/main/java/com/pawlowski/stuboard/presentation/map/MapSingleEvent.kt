package com.pawlowski.stuboard.presentation.map

import com.google.android.gms.maps.model.LatLng

sealed interface MapSingleEvent {
    data class AnimatedScrollToPage(val pageIndex: Int): MapSingleEvent
    data class AnimatedMoveMapToPosition(val position: LatLng): MapSingleEvent
}