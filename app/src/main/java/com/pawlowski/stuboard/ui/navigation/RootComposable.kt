package com.pawlowski.stuboard.ui.navigation

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.ui.theme.Green

@Composable
fun RootComposable(onNavigateToLoginScreen: () -> Unit)
{
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) },
        floatingActionButton = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            if(currentRoute == NavRoutes.MY_EVENTS)
            {
                FloatingActionButton(onClick = { navController.navigate(NavRoutes.EDIT_EVENT)
                {
                    launchSingleTop = true
                }}, backgroundColor = Green) {
                    Icon(
                        painter = painterResource(id = R.drawable.add_icon),
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            }
        }
    ) {
        NavigationGraph(navController = navController, onNavigateToLoginScreen = {
            onNavigateToLoginScreen.invoke()
        })
    }

}