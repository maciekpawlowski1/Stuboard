package com.pawlowski.stuboard.data.remote.models


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Tag(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String?
)