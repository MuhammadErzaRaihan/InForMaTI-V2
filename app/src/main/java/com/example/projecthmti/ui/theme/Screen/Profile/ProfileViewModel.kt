package com.example.projecthmti.ui.theme.Screen.Profile


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ProfileUiState(
    val name: String = "Loading...",
    val nim: String = "",
    val divisi: String = "",
    val dob: String = "",
    val email: String = ""
)

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {

        _uiState.value = ProfileUiState(
            name = "Muhammad Erza Raihan",
            nim = "2310817210027/H1G115222",
            divisi = "PSDM",
            dob = "Jakarta, 01 Januari 2000",
            email = "eza_koi@example.com"
        )
    }
}