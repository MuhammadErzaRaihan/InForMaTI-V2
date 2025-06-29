package com.example.projecthmti.ui.theme.screen.Home

import android.widget.Toast
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecthmti.data.local.db.AppDatabase
import com.example.projecthmti.data.model.Suggestion
import com.example.projecthmti.data.repository.SuggestionRepository
import com.example.projecthmti.domain.model.ScheduleItem
import com.example.projecthmti.ui.component.Header
import com.example.projecthmti.ui.components.BottomNavBar
import com.example.projecthmti.ui.theme.screen.MemberScreen
import com.example.projecthmti.ui.theme.screen.NotifScreen
import com.example.projecthmti.ui.theme.component.*
import com.example.projecthmti.ui.theme.screen.admin.AdminInboxScreen
import com.example.projecthmti.ui.theme.screen.admin.AdminScreen
import com.example.projecthmti.ui.theme.screen.chat.ChatScreen
import com.example.projecthmti.util.SessionManager
import kotlinx.coroutines.launch

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
        factory = HomeViewModelFactory(db.scheduleDao(), db.userDao())
    )

    val uiState by homeViewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    if (uiState.showSuggestionDialog) {
        SuggestionDialog(
            onDismiss = { homeViewModel.onSuggestionDialogDismiss() },
            onSend = { message ->
                coroutineScope.launch {
                    val suggestionRepo = SuggestionRepository()
                    val senderId = SessionManager.loggedInUserEmail ?: "unknown"
                    val senderName = homeViewModel.getCurrentUserName()

                    val suggestion = Suggestion(
                        senderId = senderId,
                        senderName = senderName,
                        message = message
                    )
                    suggestionRepo.sendSuggestion(suggestion)

                    Toast.makeText(context, "Kritik & Saran Terkirim!", Toast.LENGTH_SHORT).show()
                    homeViewModel.onSuggestionDialogDismiss()
                }
            }
        )
    }

    if (uiState.showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { homeViewModel.onLogoutDialogDismiss() },
            title = { Text("Konfirmasi Keluar") },
            text = { Text("Apakah Anda yakin ingin keluar?") },
            confirmButton = {
                TextButton(onClick = {
                    homeViewModel.onLogoutDialogDismiss()
                    onLogout()
                }) { Text("Ya") }
            },
            dismissButton = {
                TextButton(onClick = { homeViewModel.onLogoutDialogDismiss() }) { Text("Tidak") }
            }
        )
    }

    Scaffold(
        topBar = { Header(onProfileClick = { homeViewModel.showProfileSidebar() }) },
        bottomBar = {
            BottomNavBar(
                selectedIndex = uiState.selectedBottomNavIndex,
                onItemSelected = { homeViewModel.onBottomNavIndexChange(it) },
                onLogoutClick = { homeViewModel.onLogoutClicked() }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            val userRole = SessionManager.loggedInUserRole
            if (uiState.userRole == "ADMIN") {
                AdminDashboard(selectedIndex = uiState.selectedBottomNavIndex)
            } else {
                MemberDashboard(
                    selectedIndex = uiState.selectedBottomNavIndex,
                    upcomingSchedules = uiState.upcomingSchedules,
                    onNavigateToSchedule = onNavigateToSchedule,
                    onScheduleCardClick = { homeViewModel.onScheduleCardClicked(it) }
                )
            }

            ProfileSidebar(
                isVisible = uiState.isProfileSidebarVisible,
                onDismiss = { homeViewModel.dismissProfileSidebar() },
                onProfileClick = onNavigateToProfile,
                onSettingsClick = onNavigateToSettings,
                onSuggestionClick = { homeViewModel.onSuggestionClicked() },
                onLogoutClick = { homeViewModel.onLogoutClicked() }
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

// Konten yang ditampilkan untuk Pengguna Biasa (MEMBER)
@Composable
fun MemberDashboard(
    selectedIndex: Int,
    upcomingSchedules: List<ScheduleItem>,
    onNavigateToSchedule: () -> Unit,
    onScheduleCardClick: (ScheduleItem) -> Unit
) {
    when (selectedIndex) {
        0 -> { // Beranda
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
        1 -> MemberScreen()
        2 -> ChatScreen()
        3 -> NotifScreen()
    }
}

// Konten yang ditampilkan untuk ADMIN
@Composable
fun AdminDashboard(selectedIndex: Int) {
    when (selectedIndex) {
        0 -> AdminScreen()      // Dasbor Analitik
        2 -> ChatScreen()
        3 -> AdminInboxScreen() // Inbox Kritik & Saran
        else -> AdminScreen()   // Default ke dasbor
    }
}
