package com.pawlowski.stuboard.data.local

import com.google.android.gms.maps.model.LatLng
import com.pawlowski.stuboard.presentation.edit_event.Organisation


data class FullEventEntity(
    val tittle: String = "",
    val sinceTime: Long? = null,
    val toTime: Long? = null,
    val filtersJson: String = "",
    val isOnline: Boolean = true,
    val city: String = "",
    val streetAndNumber: String = "",
    val country: String = "",
    val placeName: String = "",
    val position: LatLng? = null,
    val organisation: Organisation? = null,
    val description: String = "",
    val site: String = "",
    val facebookSite: String = "",
)
