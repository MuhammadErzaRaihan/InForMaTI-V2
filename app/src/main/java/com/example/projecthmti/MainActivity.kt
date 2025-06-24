package com.example.projecthmti

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projecthmti.data.local.db.AppDatabase
import com.example.projecthmti.data.repository.AuthRepositoryImpl
import com.example.projecthmti.ui.theme.ProjectHMTITheme
import com.example.projecthmti.ui.theme.Screen.Home.HomeScreen
import com.example.projecthmti.ui.theme.Screen.Login.LoginScreen
import com.example.projecthmti.ui.theme.Screen.Login.LoginViewModel
import com.example.projecthmti.ui.theme.Screen.Login.LoginViewModelFactory
import com.example.projecthmti.ui.theme.Screen.Recovery.RecoveryScreen
import com.example.projecthmti.ui.theme.Screen.Register.RegistScreen
import com.example.projecthmti.ui.theme.Screen.Schedule.ScheduleScreen
import com.example.projecthmti.ui.theme.Screen.SplashScreen
// --- PERBAIKAN IMPORT DI BAWAH INI ---
import com.example.projecthmti.ui.theme.screen.profile.ProfileDetailScreen
import com.example.projecthmti.ui.theme.screen.profile.ProfileViewModel
import com.example.projecthmti.ui.theme.screen.profile.ProfileViewModelFactory
// --- SAMPAI SINI ---
import com.example.projecthmti.ui.theme.Setting.SettingScreen
import com.example.projecthmti.ui.theme.Setting.SettingsViewModel
import com.example.projecthmti.util.SessionManager

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val settingsViewModel: SettingsViewModel = viewModel()
            val settingsState by settingsViewModel.uiState.collectAsState()

            ProjectHMTITheme(darkTheme = settingsState.isDarkMode) {
                val navController = rememberNavController()
                val context = LocalContext.current
                val db = AppDatabase.getDatabase(context)

                NavHost(
                    navController = navController,
                    startDestination = AppRoute.SPLASH
                ) {
                    // ... (composable lainnya tidak berubah) ...
                    composable(AppRoute.SPLASH) {
                        SplashScreen {
                            navController.navigate(AppRoute.LOGIN) {
                                popUpTo(AppRoute.SPLASH) { inclusive = true }
                            }
                        }
                    }

                    composable(AppRoute.LOGIN) {
                        val authRepository = AuthRepositoryImpl(db.userDao())
                        val factory = LoginViewModelFactory(authRepository)
                        val loginViewModel: LoginViewModel = viewModel(factory = factory)

                        LoginScreen(
                            loginViewModel = loginViewModel,
                            onLoginSuccess = { email ->
                                SessionManager.loggedInUserEmail = email
                                navController.navigate(AppRoute.HOME) {
                                    popUpTo(AppRoute.LOGIN) { inclusive = true }
                                }
                            },
                            onRecovery = { navController.navigate(AppRoute.RECOVERY) },
                            onRegistClick = { navController.navigate(AppRoute.REGIST) },
                            onLoginFailed = { errorMessage ->
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }

                    composable(AppRoute.REGIST) {
                        RegistScreen(
                            onRegisterSuccess = { navController.popBackStack() },
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
                                SessionManager.loggedInUserEmail = null
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
                        val factory = ProfileViewModelFactory(db.userDao())
                        val profileViewModel: ProfileViewModel = viewModel(factory = factory)

                        ProfileDetailScreen(
                            onBackClick = { navController.popBackStack() },
                            profileViewModel = profileViewModel
                        )
                    }

                    composable(AppRoute.SETTINGS) {
                        SettingScreen(onBackClick = { navController.popBackStack() })
                    }

                    composable(AppRoute.SCHEDULE) {
                        ScheduleScreen(onBackClick = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}
