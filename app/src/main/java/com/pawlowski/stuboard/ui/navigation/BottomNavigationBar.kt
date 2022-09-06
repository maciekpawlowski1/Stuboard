package com.pawlowski.stuboard.ui.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pawlowski.stuboard.ui.theme.GrayGreen
import com.pawlowski.stuboard.ui.theme.Green
import com.pawlowski.stuboard.ui.theme.MidGrey

@Composable
fun BottomNavigationBar(navController: NavController)
{
    val items = listOf(
        BottomNavItems.Home,
        BottomNavItems.Search,
        BottomNavItems.Account
    )

    val itemRoutes = remember(items)
    {
        items.map { it.route }
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    if (itemRoutes.contains(currentRoute))
    {
        BottomNavigation(
            backgroundColor = Color.White,
            modifier = Modifier//.height(60.dp)
        ) {
            items.forEach { item ->
                BottomNavigationItem(icon= {
                    Icon(modifier = Modifier.size(24.dp),painter = painterResource(id = item.drawableId), contentDescription = "")
                },
                    selected = item.route == currentRoute,
                    label = { Text(text = stringResource(id = item.tittleId)) },
                    onClick = {
                        navController.navigate(item.route)
                        {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            navController.graph.startDestinationRoute?.let { screen_route ->
                                popUpTo(screen_route) {
                                    saveState = true
                                }
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    },
                    selectedContentColor = Green,
                    unselectedContentColor = MidGrey,
                    alwaysShowLabel = true
                )
            }
        }
    }


}