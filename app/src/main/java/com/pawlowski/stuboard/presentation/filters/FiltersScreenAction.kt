package com.pawlowski.stuboard.presentation.filters

sealed class FiltersScreenAction
{
    data class SearchTextChange(val newText: String): FiltersScreenAction()

    data class AddNewFilter(val filterModel: FilterModel): FiltersScreenAction()

    data class UnselectFilter(val filterModel: FilterModel): FiltersScreenAction()
}
