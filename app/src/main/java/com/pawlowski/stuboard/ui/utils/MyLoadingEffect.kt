package com.pawlowski.stuboard.ui.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

fun Modifier.myLoadingEffect(isLoading: Boolean): Modifier = composed {
    Modifier.placeholder(
        visible = isLoading,
        highlight = PlaceholderHighlight.shimmer(),
    )
}