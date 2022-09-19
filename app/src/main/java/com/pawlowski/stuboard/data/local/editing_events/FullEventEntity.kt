package com.pawlowski.stuboard.data.local.editing_events

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "editing_events")
data class FullEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tittle: String = "",
    val sinceTime: String? = null,
    val toTime: String? = null,
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
    val imageUrl: String?= null,
    val publishingStatus: Int = 0,
)
