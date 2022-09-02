package com.pawlowski.stuboard.presentation.search

sealed class SearchUiAction
{
    data class SaveScrollPosition(val scrollPosition: Int): SearchUiAction()
}
