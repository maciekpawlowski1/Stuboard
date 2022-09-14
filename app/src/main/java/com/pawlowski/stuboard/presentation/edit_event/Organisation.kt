package com.pawlowski.stuboard.presentation.edit_event

sealed class Organisation {
    data class Existing(
        val id: Int,
        val tittle: String,
        val imageUrl: String? = null
        ): Organisation()

    data class Custom(
        val tittle: String
    )
}
