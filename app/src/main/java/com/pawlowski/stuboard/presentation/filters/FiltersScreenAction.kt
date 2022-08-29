package com.pawlowski.stuboard.presentation.filters

sealed class FiltersScreenAction
{
    data class SearchTextChange(val newText: String): FiltersScreenAction()
}
