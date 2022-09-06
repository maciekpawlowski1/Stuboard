package com.pawlowski.stuboard.ui.navigation

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

@Composable
fun RootComposable(onNavigateToLoginScreen: () -> Unit)
{
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) {
        NavigationGraph(navController = navController, onNavigateToLoginScreen = {
            onNavigateToLoginScreen.invoke()
        })
    }

}