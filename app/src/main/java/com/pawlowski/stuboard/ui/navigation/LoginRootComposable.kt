package com.pawlowski.stuboard.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.pawlowski.stuboard.presentation.activity.AppLoginState

@Composable
fun LoginRootComposable(startingLoginState: AppLoginState)
{
    val controller = rememberNavController()
    LoginNavigationGraph(navController = controller,
    startDestination = if(startingLoginState is AppLoginState.NotLoggedIn)
        "Login"
    else
        "Root")
}