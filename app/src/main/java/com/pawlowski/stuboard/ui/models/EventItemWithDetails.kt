package com.pawlowski.stuboard.ui.models

data class EventItemWithDetails(
    val tittle: String = "",
    val dateDisplay: String = "",
    val hourDisplay: String = "",
    val place: String = "",
    val categoriesDrawablesId: List<Int> = listOf(),
    val isFree: Boolean? = null,
    val description: String = "",
    val imageUrl: String = "",
    val organisation: OrganisationItemForPreview = OrganisationItemForPreview()
)
