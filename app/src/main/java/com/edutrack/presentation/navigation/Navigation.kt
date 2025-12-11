package com.edutrack.presentation.navigation

sealed class Screen(val route: String) {
    object Launcher : Screen("launcher")
    object Login : Screen("login")
    object Progress : Screen("progress/{token}") {
        fun createRoute(token: String) = "progress/$token"
    }
}