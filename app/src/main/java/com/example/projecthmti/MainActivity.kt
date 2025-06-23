package com.example.projecthmti

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projecthmti.ui.theme.ProjectHMTITheme
import com.example.projecthmti.ui.theme.Screen.*
import com.example.projecthmti.ui.theme.Setting.SettingScreen
import com.example.projecthmti.ui.theme.Setting.SettingsViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Kita butuh ViewModel Pengaturan di tingkat tertinggi
            // untuk mengontrol tema aplikasi secara global.
            val settingsViewModel: SettingsViewModel = viewModel()
            val settingsState by settingsViewModel.uiState.collectAsState()

            ProjectHMTITheme(darkTheme = settingsState.isDarkMode) {
                val navController = rememberNavController()

                // NavHost adalah komponen utama yang mengatur perpindahan layar
                NavHost(
                    navController = navController,
                    startDestination = AppRoute.SPLASH
                ) {
                    // Setiap 'composable' mendefinisikan satu layar
                    composable(AppRoute.SPLASH) {
                        SplashScreen {
                            // Setelah splash, hapus dari back stack dan pergi ke login
                            navController.navigate(AppRoute.LOGIN) {
                                popUpTo(AppRoute.SPLASH) { inclusive = true }
                            }
                        }
                    }

                    composable(AppRoute.LOGIN) {
                        LoginScreen(
                            Succeed = {
                                navController.navigate(AppRoute.HOME) {
                                    popUpTo(AppRoute.LOGIN) { inclusive = true }
                                }
                            },
                            onRecovery = { navController.navigate(AppRoute.RECOVERY) },
                            onRegistClick = { navController.navigate(AppRoute.REGIST) }
                        )
                    }

                    composable(AppRoute.REGIST) {
                        RegistScreen(
                            onRegister = { navController.popBackStack() }, // Kembali ke login
                            onLogin = { navController.popBackStack() }
                        )
                    }

                    composable(AppRoute.RECOVERY) {
                        RecoveryScreen(
                            onRecoverySubmitted = { navController.popBackStack() },
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    composable(AppRoute.HOME) {
                        HomeScreen(
                            onLogout = {
                                navController.navigate(AppRoute.LOGIN) {
                                    popUpTo(AppRoute.HOME) { inclusive = true }
                                }
                            },
                            onNavigateToProfile = { navController.navigate(AppRoute.PROFILE_DETAIL) },
                            onNavigateToSettings = { navController.navigate(AppRoute.SETTINGS) },
                            onNavigateToSchedule = { navController.navigate(AppRoute.SCHEDULE) }
                        )
                    }

                    composable(AppRoute.PROFILE_DETAIL) {
                        ProfileDetailScreen(onBackClick = { navController.popBackStack() })
                    }

                    composable(AppRoute.SETTINGS) {
                        // ViewModel akan otomatis di-scope ke layar ini
                        SettingScreen(onBackClick = { navController.popBackStack() })
                    }

                    composable(AppRoute.SCHEDULE) {
                        ScheduleScreen(onBackClick = { navController.popBackStack() })
                    }

                    composable(AppRoute.RECOVERY) {
                        RecoveryScreen(
                            onRecoverySubmitted = { navController.popBackStack() }, // Aksi setelah submit
                            onBackClick = { navController.popBackStack() } // Aksi tombol kembali
                        )
                    }
                }
            }
        }
    }
}