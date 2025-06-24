package com.example.projecthmti.ui.theme.Screen.Home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecthmti.domain.model.ScheduleItem
import com.example.projecthmti.domain.usecase.GetSchedulesUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeUiState(
    val selectedBottomNavIndex: Int = 0,
    val isProfileSidebarVisible: Boolean = false,
    val upcomingSchedules: List<ScheduleItem> = emptyList()
)

class HomeViewModel (
    private val getSchedulesUseCase: GetSchedulesUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        observeUpcomingSchedules()
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

    fun onBottomNavIndexChange(newIndex: Int) {
        _uiState.update { it.copy(selectedBottomNavIndex = newIndex) }
    }

    fun showProfileSidebar() {
        _uiState.update { it.copy(isProfileSidebarVisible = true) }
    }

    fun dismissProfileSidebar() {
        _uiState.update { it.copy(isProfileSidebarVisible = false) }
    }
}