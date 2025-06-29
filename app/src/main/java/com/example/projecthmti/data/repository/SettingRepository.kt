package com.example.projecthmti.data.repository

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

    // StateFlow untuk Dark Mode
    private val _isDarkMode = MutableStateFlow(prefs.getBoolean(KEY_DARK_MODE, false))
    val isDarkMode: StateFlow<Boolean> = _isDarkMode

    // StateFlow untuk Bahasa
    private val _isIndonesianLanguage = MutableStateFlow(prefs.getBoolean(KEY_LANGUAGE, true)) // Default ke Bahasa Indonesia
    val isIndonesianLanguage: StateFlow<Boolean> = _isIndonesianLanguage

    fun setDarkMode(isDark: Boolean) {
        _isDarkMode.value = isDark
        prefs.edit().putBoolean(KEY_DARK_MODE, isDark).apply()
    }

    fun setLanguage(isIndonesian: Boolean) {
        _isIndonesianLanguage.value = isIndonesian
        prefs.edit().putBoolean(KEY_LANGUAGE, isIndonesian).apply()
    }

    companion object {
        private const val KEY_DARK_MODE = "key_dark_mode"
        private const val KEY_LANGUAGE = "key_language_is_indonesian"
    }
}
