package com.pawlowski.stuboard.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pawlowski.stuboard.ui.other_screens.EventDetailsScreen
import com.pawlowski.stuboard.ui.other_screens.FiltersScreen
import com.pawlowski.stuboard.ui.other_screens.MapScreen
import com.pawlowski.stuboard.ui.screens_in_bottom_navigation_related.screens.HomeScreen
import com.pawlowski.stuboard.ui.screens_in_bottom_navigation_related.screens.SearchScreen

@Composable
fun NavigationGraph(navController: NavHostController)
{
    NavHost(navController = navController, startDestination = NavRoutes.HOME)
    {
        composable(route = NavRoutes.HOME)
        {
            HomeScreen(onNavigateToSearchScreen = {
                navController.navigate(NavRoutes.SEARCH)
                {
                    navController.graph.startDestinationRoute?.let { screen_route ->
                        popUpTo(screen_route) {
                            saveState = true
                        }
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
                onNavigateToEventDetailScreen = { eventId ->
                    navController.navigate("${NavRoutes.EVENT_DETAILS.basicRoute}/${eventId}")
                },
                onNavigateToMapScreen = {
                    navController.navigate(NavRoutes.MAP)
                    {
                        launchSingleTop = true
                    }
                },
                onNavigateToSearchScreenWithParameter = { categoryId ->
                    navController.navigate(NavRoutes.SEARCH)
                    {
                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(route = NavRoutes.SEARCH)
        {
            SearchScreen(onNavigateToEventDetailsScreen = { eventId ->
                navController.navigate("${NavRoutes.EVENT_DETAILS.basicRoute}/${eventId}")
            }, onNavigateToFiltersScreen = {
                navController.navigate(NavRoutes.FILTERS)
            })

        }

        composable(route = NavRoutes.EVENT_DETAILS.fullRoute,
            arguments = listOf(
                navArgument("eventId") { NavType.StringType }
            )
        )
        {
            EventDetailsScreen()
        }

        composable(route = NavRoutes.MAP)
        {
            MapScreen(onNavigateBack = {
                navController.popBackStack()
            }, onNavigateToEventDetailsScreen = { eventId ->
                navController.navigate("${NavRoutes.EVENT_DETAILS.basicRoute}/${eventId}")
            })

        }

        composable(route = NavRoutes.FILTERS)
        {
            FiltersScreen(onNavigateBack = {
                navController.popBackStack()
            })
        }

    }
}