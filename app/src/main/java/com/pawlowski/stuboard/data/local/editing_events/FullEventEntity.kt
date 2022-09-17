package com.pawlowski.stuboard.data.local.editing_events

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.pawlowski.stuboard.presentation.edit_event.Organisation


@Entity(tableName = "editing_events")
data class FullEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tittle: String = "",
    val sinceTime: Long? = null,
    val toTime: Long? = null,
    val filtersJson: String = "",
    val isOnline: Boolean = true,
    val city: String = "",
    val streetAndNumber: String = "",
    val country: String = "",
    val placeName: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val organisationId: Int? = null,
    val customOrganisationTittle: String? = null,
    val description: String = "",
    val site: String = "",
    val facebookSite: String = "",
)
