package com.example.projecthmti.ui.theme.Screen.Home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecthmti.data.local.db.AppDatabase
import com.example.projecthmti.domain.model.ScheduleItem
import com.example.projecthmti.ui.component.Header
import com.example.projecthmti.ui.components.BottomNavBar
import com.example.projecthmti.ui.theme.Screen.MemberScreen
import com.example.projecthmti.ui.theme.Screen.NotifScreen
import com.example.projecthmti.ui.theme.component.*

@Preview
@Composable
fun HomeScreen(
    onLogout: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToSchedule: () -> Unit = {}
) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(db.scheduleDao())
    )

    val uiState by homeViewModel.uiState.collectAsState()

    // --- TAMPILKAN DIALOG KONFIRMASI LOGOUT ---
    if (uiState.showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { homeViewModel.onLogoutDialogDismiss() },
            title = { Text("Konfirmasi Keluar") },
            text = { Text("Apakah Anda yakin ingin keluar dari aplikasi?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        homeViewModel.onLogoutDialogDismiss()
                        onLogout() // Panggil fungsi logout asli dari MainActivity
                    }
                ) {
                    Text("Ya")
                }
            },
            dismissButton = {
                TextButton(onClick = { homeViewModel.onLogoutDialogDismiss() }) {
                    Text("Tidak")
                }
            }
        )
    }

    Scaffold(
        topBar = { Header(onProfileClick = { homeViewModel.showProfileSidebar() }) },
        bottomBar = {
            BottomNavBar(
                selectedIndex = uiState.selectedBottomNavIndex,
                onItemSelected = { homeViewModel.onBottomNavIndexChange(it) },
                onLogoutClick = { homeViewModel.onLogoutClicked() } // Panggil fungsi ViewModel
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            MainContent(
                selectedIndex = uiState.selectedBottomNavIndex,
                upcomingSchedules = uiState.upcomingSchedules,
                onNavigateToSchedule = onNavigateToSchedule,
                onScheduleCardClick = { schedule ->
                    homeViewModel.onScheduleCardClicked(schedule)
                }
            )

            ProfileSidebar(
                isVisible = uiState.isProfileSidebarVisible,
                onDismiss = { homeViewModel.dismissProfileSidebar() },
                onProfileClick = {
                    homeViewModel.dismissProfileSidebar()
                    onNavigateToProfile()
                },
                onSettingsClick = {
                    homeViewModel.dismissProfileSidebar()
                    onNavigateToSettings()
                },
                onLogoutClick = {
                    homeViewModel.onLogoutClicked() // Tombol logout di sidebar juga menampilkan dialog
                }
            )

            uiState.selectedScheduleDetail?.let { schedule ->
                ScheduleDetailDialog(
                    schedule = schedule,
                    onDismiss = { homeViewModel.onScheduleDetailDismiss() }
                )
            }
        }
    }
}

@Composable
fun MainContent(
    selectedIndex: Int,
    upcomingSchedules: List<ScheduleItem>,
    onNavigateToSchedule: () -> Unit,
    onScheduleCardClick: (ScheduleItem) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        when (selectedIndex) {
            0 -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item { Announcement() }
                    item { MenuDivisiSection() }
                    item {
                        UpcomingEventSection(
                            schedules = upcomingSchedules,
                            onAddScheduleClick = onNavigateToSchedule,
                            onScheduleClick = onScheduleCardClick
                        )
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
            1 -> {
                MemberScreen()
            }
            3 -> {
                NotifScreen()
            }
            else -> {}
        }
    }
}
