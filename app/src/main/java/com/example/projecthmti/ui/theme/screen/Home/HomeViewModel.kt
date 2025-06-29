package com.example.projecthmti.ui.theme.screen.Home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecthmti.data.local.db.dao.UserDao
import com.example.projecthmti.domain.model.ScheduleItem
import com.example.projecthmti.domain.usecase.GetSchedulesUseCase
import com.example.projecthmti.util.SessionManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeUiState(
    val selectedBottomNavIndex: Int = 0,
    val isProfileSidebarVisible: Boolean = false,
    val upcomingSchedules: List<ScheduleItem> = emptyList(),
    val selectedScheduleDetail: ScheduleItem? = null,
    val showLogoutDialog: Boolean = false,
    val showSuggestionDialog: Boolean = false,
    val userRole: String = "MEMBER"
)

class HomeViewModel(
    private val getSchedulesUseCase: GetSchedulesUseCase,
    private val userDao: UserDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadCurrentUserRole()
        observeUpcomingSchedules()
    }
    private fun loadCurrentUserRole() {
        val role = SessionManager.loggedInUserRole ?: "MEMBER"
        _uiState.update { it.copy(userRole = role) }
    }

    private fun observeUpcomingSchedules() {
        viewModelScope.launch {
            getSchedulesUseCase()
                .catch { e ->
                    println("Error observing schedules: ${e.message}")
                }
                .collect { scheduleList ->
                    _uiState.update { it.copy(upcomingSchedules = scheduleList.take(3)) }
                }
        }
    }

    suspend fun getCurrentUserName(): String {
        val email = SessionManager.loggedInUserEmail ?: return "Pengguna Anonim"
        val user = userDao.findUserByEmail(email)
        return user?.name ?: "Pengguna"
    }

    fun onBottomNavIndexChange(newIndex: Int) {
        _uiState.update { it.copy(selectedBottomNavIndex = newIndex) }
    }

    fun showProfileSidebar() {
        _uiState.update { it.copy(isProfileSidebarVisible = true) }
    }

    fun dismissProfileSidebar() {
        _uiState.update { it.copy(isProfileSidebarVisible = false) }
    }

    fun onScheduleCardClicked(schedule: ScheduleItem) {
        _uiState.update { it.copy(selectedScheduleDetail = schedule) }
    }

    fun onScheduleDetailDismiss() {
        _uiState.update { it.copy(selectedScheduleDetail = null) }
    }

    fun onLogoutClicked() {
        _uiState.update { it.copy(showLogoutDialog = true) }
    }

    fun onLogoutDialogDismiss() {
        _uiState.update { it.copy(showLogoutDialog = false) }
    }

    fun onSuggestionClicked() {
        _uiState.update { it.copy(showSuggestionDialog = true) }
    }

    fun onSuggestionDialogDismiss() {
        _uiState.update { it.copy(showSuggestionDialog = false) }
    }
}
