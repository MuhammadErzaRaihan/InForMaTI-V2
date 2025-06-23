package com.example.projecthmti.ui.theme.Setting

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Menggunakan AppSettings sebagai UI State
// data class AppSettings(
//     val isDarkMode: Boolean = false,
//     val isIndonesianLanguage: Boolean = true
// )

class SettingsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AppSettings())
    val uiState: StateFlow<AppSettings> = _uiState.asStateFlow()

    init {
        // Di sini logika untuk memuat pengaturan yang tersimpan dari Repository
        // Contoh: _uiState.value = settingsRepository.loadSettings()
    }

    fun onThemeChanged(isDarkMode: Boolean) {
        _uiState.update { it.copy(isDarkMode = isDarkMode) }
        // Di sini logika untuk menyimpan pengaturan tema ke Repository
        // Contoh: settingsRepository.saveTheme(isDarkMode)
    }

    fun onLanguageChanged(isIndonesian: Boolean) {
        _uiState.update { it.copy(isIndonesianLanguage = isIndonesian) }
        // Di sini logika untuk menyimpan pengaturan bahasa ke Repository
        // Contoh: settingsRepository.saveLanguage(isIndonesian)
    }
}