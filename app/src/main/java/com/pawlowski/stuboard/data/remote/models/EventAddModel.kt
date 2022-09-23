package com.pawlowski.stuboard.data.remote.models


import com.google.gson.annotations.SerializedName

data class EventAddModel(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("background")
    val background: String?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("endDate")
    val endDate: String,
    @SerializedName("facebook")
    val facebook: String?,
    @SerializedName("language")
    val language: String?,
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
    @SerializedName("registration")
    val registration: Boolean,
    @SerializedName("shortDescription")
    val shortDescription: String?,
    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("tags")
    val tags: List<Tag>?,
    @SerializedName("thumbnail")
    val thumbnail: String?,
    @SerializedName("tickets")
    val tickets: Boolean,
    @SerializedName("website")
    val website: String?
)