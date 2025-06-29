package com.example.projecthmti.ui.theme.Setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecthmti.data.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class AppSettings(
    val isDarkMode: Boolean = false,
    val isIndonesianLanguage: Boolean = true
)

class SettingsViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {

    val uiState: StateFlow<AppSettings> = combine(
        settingsRepository.isDarkMode,
        settingsRepository.isIndonesianLanguage
    ) { isDark, isIndonesian ->
        AppSettings(isDarkMode = isDark, isIndonesianLanguage = isIndonesian)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AppSettings()
    )

    fun onThemeChanged(isDarkMode: Boolean) {
        settingsRepository.setDarkMode(isDarkMode)
    }

    fun onLanguageChanged(isIndonesian: Boolean) {
        settingsRepository.setLanguage(isIndonesian)
    }
}
