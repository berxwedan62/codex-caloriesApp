package com.lokma.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lokma.app.ui.screens.AddMealScreen
import com.lokma.app.ui.screens.FoodLibraryScreen
import com.lokma.app.ui.screens.HistoryScreen
import com.lokma.app.ui.screens.HomeScreen
import com.lokma.app.ui.screens.SettingsScreen
import com.lokma.app.ui.screens.SplashScreen
import com.lokma.app.ui.screens.WeightScreen
import kotlinx.coroutines.delay

@Composable
fun LokmaApp() {
    var showSplash by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(2_000)
        showSplash = false
    }

    if (showSplash) {
        SplashScreen()
        return
    }

    val navController = rememberNavController()
    val items = listOf(
        NavRoutes.Home,
        NavRoutes.AddMeal,
        NavRoutes.Library,
        NavRoutes.History,
        NavRoutes.Weight,
        NavRoutes.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry = navController.currentBackStackEntryAsState().value
                val destination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        selected = destination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            val icon = when (screen) {
                                NavRoutes.Home -> Icons.Default.Home
                                NavRoutes.AddMeal -> Icons.Default.Add
                                NavRoutes.Library -> Icons.Default.List
                                NavRoutes.History -> Icons.Default.DateRange
                                NavRoutes.Weight -> Icons.Default.ShowChart
                                NavRoutes.Settings -> Icons.Default.Settings
                            }
                            Icon(icon, contentDescription = screen.label)
                        },
                        label = { Text(screen.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = NavRoutes.Home.route, modifier = Modifier.padding(innerPadding)) {
            composable(NavRoutes.Home.route) { HomeScreen() }
            composable(NavRoutes.AddMeal.route) { AddMealScreen() }
            composable(NavRoutes.Library.route) { FoodLibraryScreen() }
            composable(NavRoutes.History.route) { HistoryScreen() }
            composable(NavRoutes.Weight.route) { WeightScreen() }
            composable(NavRoutes.Settings.route) { SettingsScreen() }
        }
    }
}
