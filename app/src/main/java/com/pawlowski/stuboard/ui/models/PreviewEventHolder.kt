package com.pawlowski.stuboard.ui.models

data class PreviewEventHolder(
    val eventWithoutLocation: EventItemForPreview? = null,
    val eventWithLocation: EventItemForPreviewWithLocation? = null
)