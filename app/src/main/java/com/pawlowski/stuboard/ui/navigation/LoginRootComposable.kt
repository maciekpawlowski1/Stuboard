package com.pawlowski.stuboard.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

@Composable
fun LoginRootComposable()
{
    val controller = rememberNavController()
    LoginNavigationGraph(navController = controller)
}