package com.example.projecthmti

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projecthmti.data.local.db.AppDatabase
import com.example.projecthmti.data.repository.AuthRepositoryImpl
import com.example.projecthmti.data.repository.SettingsRepository
import com.example.projecthmti.ui.theme.ProjectHMTITheme
import com.example.projecthmti.ui.theme.screen.Home.HomeScreen
import com.example.projecthmti.ui.theme.screen.Login.LoginScreen
import com.example.projecthmti.ui.theme.screen.Login.LoginViewModel
import com.example.projecthmti.ui.theme.screen.Login.LoginViewModelFactory
import com.example.projecthmti.ui.theme.screen.Recovery.RecoveryScreen
import com.example.projecthmti.ui.theme.screen.Register.RegistScreen
import com.example.projecthmti.ui.theme.screen.Schedule.ScheduleScreen
import com.example.projecthmti.ui.theme.screen.SplashScreen
import com.example.projecthmti.ui.theme.Setting.SettingScreen
import com.example.projecthmti.ui.theme.Setting.SettingsViewModel
import com.example.projecthmti.ui.theme.Setting.SettingsViewModelFactory
import com.example.projecthmti.ui.theme.screen.profile.ProfileDetailScreen
import com.example.projecthmti.ui.theme.screen.profile.ProfileViewModel
import com.example.projecthmti.ui.theme.screen.profile.ProfileViewModelFactory
import com.example.projecthmti.util.SessionManager
import java.util.*

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (!isGranted) {
                Toast.makeText(this, "Notifikasi tidak akan muncul tanpa izin.", Toast.LENGTH_SHORT).show()
            }
        }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun setLocale(context: Context, lang: String) {
        val currentLocale = context.resources.configuration.locales.get(0)
        if (currentLocale.language != lang) {
            val locale = Locale(lang)
            Locale.setDefault(locale)
            val config = Configuration(context.resources.configuration)
            config.setLocale(locale)
            context.createConfigurationContext(config)
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askNotificationPermission()

        setContent {
            val context = LocalContext.current
            val settingsRepository = remember { SettingsRepository(context) }
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModelFactory(settingsRepository)
            )

            // Observasi uiState tunggal
            val uiState by settingsViewModel.uiState.collectAsState()

            // Terapkan lokal bahasa saat komposisi ulang
            setLocale(context, if (uiState.isIndonesianLanguage) "in" else "en")

            // Terapkan tema
            ProjectHMTITheme(darkTheme = uiState.isDarkMode) {
                val navController = rememberNavController()
                val db = AppDatabase.getDatabase(context)

                NavHost(
                    navController = navController,
                    startDestination = AppRoute.SPLASH
                ) {
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
