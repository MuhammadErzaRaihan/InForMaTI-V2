package com.example.projecthmti.ui.theme.Screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import com.example.projecthmti.ui.theme.component.Event

data class HomeUiState(
    val selectedBottomNavIndex: Int = 0,
    val isProfileSidebarVisible: Boolean = false,
    val upcomingEvents: List<Event> = emptyList()
)

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        // Muat data yang dibutuhkan oleh HomeScreen
        loadUpcomingEvents()
    }

    private fun loadUpcomingEvents() {
        // Data ini idealnya datang dari sebuah Repository
        val events = listOf(
            Event("RAPAT PKKMB", "A-14", "13.00"),
            Event("RAPAT LKD", "A-12", "13.00"),
            Event("FUTSAL BARENG", "GOR BJM", "19.00")
        )
        _uiState.update { it.copy(upcomingEvents = events) }
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