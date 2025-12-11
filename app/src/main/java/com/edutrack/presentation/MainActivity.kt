package com.edutrack.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.edutrack.data.storage.TokenStorage
import com.edutrack.presentation.auth.LoginScreen
import com.edutrack.presentation.launcher.LauncherScreen  // ← импорт
import com.edutrack.presentation.navigation.Screen
import com.edutrack.presentation.progress.MainProgressScreen
import dagger.hilt.android.AndroidEntryPoint
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

                    NavHost(
                        navController = navController,
                        startDestination = Screen.Launcher.route  // ← теперь старт здесь
                    ) {
                        composable(Screen.Launcher.route) {
                            LauncherScreen(
                                navController = navController,
                                tokenStorage = tokenStorage
                            )
                        }
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