package com.pawlowski.stuboard.ui.navigation

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

@Composable
fun RootComposable()
{
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) {
        NavigationGraph(navController = navController)
    }

}