package com.pawlowski.stuboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.pawlowski.stuboard.ui.navigation_items.BottomNavItems
import com.pawlowski.stuboard.ui.navigation_items.HomeScreen
import com.pawlowski.stuboard.ui.navigation_items.SearchScreen
import com.pawlowski.stuboard.ui.theme.StuboardTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StuboardTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MyApp()
                }
            }
        }

        try {
            val apiKey: String = System.getenv("APP_CENTER_KEY")?:getString(R.string.appCenterKey)
            AppCenter.start(application, apiKey, Analytics::class.java, Crashes::class.java)
        }
        catch (e: Exception)
        {}

    }
}
@Composable
fun MyApp()
{
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) {
        NavigationGraph(navController = navController)
    }

}

@Composable
fun NavigationGraph(navController: NavHostController)
{
    NavHost(navController = navController, startDestination = BottomNavItems.Home.route)
    {
        composable(route = BottomNavItems.Home.route)
        {
            HomeScreen()
        }
        composable(route = BottomNavItems.Search.route)
        {
            SearchScreen()
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController)
{
    val items = listOf(
        BottomNavItems.Home,
        BottomNavItems.Search
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomNavigation(
        backgroundColor = Color.White,
        modifier = Modifier.height(70.dp)
    ) {
        items.forEach { item ->
            BottomNavigationItem(icon= {
                Icon(painterResource(id = item.drawableId), contentDescription = "")
                                       },
                selected = item.route == currentRoute,
                label = { Text(text = stringResource(id = item.tittleId))},
                onClick = {
                    navController.navigate(item.route)
                    {
                        //TODO
                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                selectedContentColor = Color.Green,
                unselectedContentColor = Color.Black,
                alwaysShowLabel = true
            )
        }
    }
    
}


