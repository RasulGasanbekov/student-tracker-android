package com.edutrack.presentation.launcher

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.edutrack.data.storage.TokenStorage
import com.edutrack.presentation.navigation.Screen

@Composable
fun LauncherScreen(
    navController: NavController,
    tokenStorage: TokenStorage
) {
    LaunchedEffect(Unit) {
        val token = tokenStorage.getToken()
        if (token != null) {
            navController.navigate(Screen.Progress.createRoute(token)) {
                popUpTo(Screen.Launcher.route) { inclusive = true }
            }
        } else {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Launcher.route) { inclusive = true }
            }
        }
    }

    // Показываем индикатор загрузки (на мгновение)
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}