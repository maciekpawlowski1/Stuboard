package com.pawlowski.stuboard.data.remote.models


import com.google.gson.annotations.SerializedName

data class EventsResponseItem(
    @SerializedName("background")
    val background: String?,
    @SerializedName("city")
    val city: String,
    @SerializedName("endDate")
    val endDate: String,
    @SerializedName("facebook")
    val facebook: String?,
    @SerializedName("id")
    val id: String,
    @SerializedName("language")
    val language: String?,
    @SerializedName("lastModified")
    val lastModified: String?,
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("location")
    val location: String?,
    @SerializedName("longitude")
    val longitude: Double?,
    @SerializedName("name")
    val name: String,
    @SerializedName("online")
    val online: Boolean,
    @SerializedName("organization")
    val organization: String,
    @SerializedName("ownerID")
    val ownerID: String?,
    @SerializedName("published")
    val published: Boolean,
    @SerializedName("registration")
    val registration: Boolean,
    @SerializedName("shortDescription")
    val shortDescription: String,
    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("studentGovernmentId")
    val studentGovernmentId: Int?,
    @SerializedName("tags")
    val tags: List<Tag?>?,
    @SerializedName("thumbnail")
    val thumbnail: String,
    @SerializedName("tickets")
    val tickets: Boolean,
    @SerializedName("upvotes")
    val upvotes: Int?,
    @SerializedName("website")
    val website: String?
)