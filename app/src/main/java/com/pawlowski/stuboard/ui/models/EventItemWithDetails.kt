package com.pawlowski.stuboard.ui.models

data class EventItemWithDetails(
    val tittle: String = "",
    val dateDisplay: String = "",
    val hourDisplay: String = "",
    val place: String = "",
    val categoriesDrawablesId: List<Int> = listOf(),
    val price: Float = -1.0f,
    val description: String = "",
    val imageUrl: String = "",
    val organisation: OrganisationItemForPreview = OrganisationItemForPreview()
)
