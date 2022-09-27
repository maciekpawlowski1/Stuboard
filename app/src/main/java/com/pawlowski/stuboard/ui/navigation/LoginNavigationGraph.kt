package com.pawlowski.stuboard.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pawlowski.stuboard.presentation.activity.AppLoginState
import com.pawlowski.stuboard.ui.login_screens.LoginNavigationCallbacks
import com.pawlowski.stuboard.ui.login_screens.LoginScreen
import com.pawlowski.stuboard.ui.register_screen.RegisterScreen

@Composable
fun LoginNavigationGraph(
    navController: NavHostController,
    startDestination: String,
)
{
    NavHost(navController = navController, startDestination = startDestination)
    {
        composable(route = "Login")
        {
            LoginScreen(navigationCallbacks = LoginNavigationCallbacks(
                onNavigateToRegisterScreen = {
                    navController.navigate("Register")
                    {
                        launchSingleTop = true
                    }
                },
                onNavigateToRoot = {
                    navController.navigate("Root")
                    {
                        popUpTo("Login")
                        {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            ))
        }
        composable(route = "Register")
        {
            RegisterScreen(onNavigateBack = {
                navController.popBackStack()
            }, onNavigateToRoot = {
                navController.navigate("Root")
                {
                    popUpTo("Login")
                    {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            })
        }
        composable(route = "Root")
        {
            RootComposable(onNavigateToLoginScreen = {
                navController.navigate("Login")
                {
                    popUpTo("Root")
                    {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            })
        }
    }
}