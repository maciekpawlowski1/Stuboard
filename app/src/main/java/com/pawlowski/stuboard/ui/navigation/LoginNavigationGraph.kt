package com.pawlowski.stuboard.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pawlowski.stuboard.ui.login_screens.LoginScreen
import com.pawlowski.stuboard.ui.register_screen.RegisterScreen

@Composable
fun LoginNavigationGraph(navController: NavHostController)
{
    NavHost(navController = navController, startDestination = "Login")
    {
        composable(route = "Login")
        {
            LoginScreen(onNavigateToRegisterScreen = {
                navController.navigate("Register")
            })
        }
        composable(route = "Register")
        {
            RegisterScreen(onNavigateBack = {
                navController.popBackStack()
            })
        }
    }
}