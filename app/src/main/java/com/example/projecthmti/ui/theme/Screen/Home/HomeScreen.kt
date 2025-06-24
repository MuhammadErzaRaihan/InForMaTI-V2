package com.example.projecthmti.ui.theme.Screen.Home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecthmti.data.local.db.AppDatabase
import com.example.projecthmti.domain.model.ScheduleItem // Ganti Event dengan ScheduleItem
import com.example.projecthmti.ui.component.Header
import com.example.projecthmti.ui.components.BottomNavBar
import com.example.projecthmti.ui.theme.Screen.NotifScreen
import com.example.projecthmti.ui.theme.Screen.OnlineMemberList
import com.example.projecthmti.ui.theme.component.Announcement
import com.example.projecthmti.ui.theme.component.MenuDivisiSection
import com.example.projecthmti.ui.theme.component.ProfileSidebar
import com.example.projecthmti.ui.theme.component.UpcomingEventSection


@Preview
@Composable
fun HomeScreen(
    onLogout: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToSchedule: () -> Unit = {}
    // HAPUS deklarasi homeViewModel dari parameter
) {
    // Inisialisasi ViewModel di dalam body, sama seperti ScheduleScreen
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(db.scheduleDao())
    )

    val uiState by homeViewModel.uiState.collectAsState()

    Scaffold(
        topBar = { Header(onProfileClick = { homeViewModel.showProfileSidebar() }) },
        bottomBar = {
            BottomNavBar(
                selectedIndex = uiState.selectedBottomNavIndex,
                onItemSelected = { homeViewModel.onBottomNavIndexChange(it) }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            MainContent(
                selectedIndex = uiState.selectedBottomNavIndex,
                // PERBAIKAN: Kirim data jadwal yang benar dari uiState
                upcomingSchedules = uiState.upcomingSchedules,
                onNavigateToSchedule = onNavigateToSchedule
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
                    homeViewModel.dismissProfileSidebar()
                    onLogout()
                }
            )
        }
    }
}

@Composable
fun MainContent(
    selectedIndex: Int,
    // PERBAIKAN: Ganti parameter dari List<Event> menjadi List<ScheduleItem>
    upcomingSchedules: List<ScheduleItem>,
    onNavigateToSchedule: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        when (selectedIndex) {
            0 -> {
                Announcement()
                MenuDivisiSection()
                UpcomingEventSection(
                    // PERBAIKAN: Teruskan parameter yang benar
                    schedules = upcomingSchedules,
                    onAddScheduleClick = onNavigateToSchedule
                )
                Spacer(modifier = Modifier.height(80.dp))
            }
            1 -> {
                OnlineMemberList()
                Spacer(modifier = Modifier.height(80.dp))
            }
            3 -> NotifScreen()
            else -> {
                //  incase ada konten lain
            }
        }
    }
}