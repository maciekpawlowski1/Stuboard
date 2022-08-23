package com.pawlowski.stuboard.ui.navigation_items

import com.pawlowski.stuboard.R

sealed class BottomNavItems(val route: String, val tittleId: Int, val drawableId: Int)
{
    object Home: BottomNavItems("Home", R.string.home, R.drawable.home_icon)
    object Search: BottomNavItems("Search", R.string.search, R.drawable.search_icon)
}
