package com.pawlowski.stuboard.presentation.filters

sealed class FiltersScreenAction
{
    data class SearchTextChange(val newText: String): FiltersScreenAction()

    data class AddNewTextFilter(val text: String): FiltersScreenAction()
}
