package com.lokma.app.ui.navigation

sealed class NavRoutes(val route: String, val label: String) {
    data object Home : NavRoutes("home", "Home")
    data object AddMeal : NavRoutes("add_meal", "Add")
    data object Library : NavRoutes("library", "Library")
    data object History : NavRoutes("history", "History")
    data object Weight : NavRoutes("weight", "Weight")
    data object Settings : NavRoutes("settings", "Settings")
}
