package com.edutrack.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.edutrack.data.storage.TokenStorage
import com.edutrack.presentation.auth.LoginScreen
import com.edutrack.presentation.navigation.Screen
import com.edutrack.presentation.progress.MainProgressScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var tokenStorage: TokenStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()

                    // Проверяем токен при создании NavHost
                    LaunchedEffect(Unit) {
                        val token = tokenStorage.getToken()
                        if (token != null) {
                            // Немного ждем, чтобы NavHost успел инициализироваться
                            delay(100)
                            navController.navigate(Screen.Progress.createRoute(token)) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                    }

                    // NavHost создается ВСЕГДА
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Login.route
                    ) {
                        composable(Screen.Login.route) {
                            LoginScreen(navController = navController)
                        }
                        composable(Screen.Progress.route) { backStackEntry ->
                            val token = backStackEntry.arguments?.getString("token") ?: ""
                            MainProgressScreen(
                                navController = navController,
                                token = token
                            )
                        }
                    }
                }
            }
        }
    }
}

