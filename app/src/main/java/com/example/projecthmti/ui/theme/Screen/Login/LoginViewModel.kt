package com.example.projecthmti.ui.theme.Screen.Login

import androidx.lifecycle.ViewModel
import com.example.projecthmti.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LoginUiState(
    val username: String = "",
    val password: String = ""
)

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    // _uiState bersifat private dan mutable, hanya bisa diubah di dalam ViewModel
    private val _uiState = MutableStateFlow(LoginUiState())

    // uiState bersifat public dan read-only, untuk diobservasi oleh UI
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // Fungsi untuk menangani event perubahan username dari UI
    fun onUsernameChange(username: String) {
        _uiState.update { currentState ->
            currentState.copy(username = username)
        }
    }

    // Fungsi untuk menangani event perubahan password dari UI
    fun onPasswordChange(password: String) {
        _uiState.update { currentState ->
            currentState.copy(password = password)
        }
    }

    // Fungsi untuk menangani event klik tombol login
    fun onLoginClick() {
        val username = _uiState.value.username
        val password = _uiState.value.password

        if (username.isNotBlank() && password.isNotBlank()) {
            // Lakukan proses login (misalnya memanggil Repository)
            println("Proses login dengan username: $username dan password: $password")
            // Setelah berhasil, kirim event navigasi ke UI
        } else {
            println("Username dan password tidak boleh kosong")
        }
    }
}