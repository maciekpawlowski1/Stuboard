package com.pawlowski.stuboard.ui.navigation

import com.pawlowski.stuboard.R

sealed class BottomNavItems(val route: String, val tittleId: Int, val drawableId: Int)
{
    object Home: BottomNavItems(NavRoutes.HOME, R.string.home, R.drawable.home_icon)
    object Search: BottomNavItems(NavRoutes.SEARCH, R.string.search, R.drawable.search_icon)
}

object NavRoutes
{
    const val HOME = "Home"
    const val SEARCH = "Search"
    val EVENT_DETAILS = BasicRoute("EventDetails/{eventId}", "EventDetails")
}

data class BasicRoute(val fullRoute: String, val basicRoute: String)
