package com.example.projecthmti.ui.theme.screen.Login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecthmti.domain.repository.AuthRepository
import com.example.projecthmti.util.SessionManager // <-- TAMBAHKAN IMPORT INI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val username: String = "",
    val password: String = ""
)

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onUsernameChange(username: String) {
        _uiState.update { it.copy(username = username) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    // Fungsi ini menangani klik tombol login
    fun onLoginClick(onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val username = _uiState.value.username
        val password = _uiState.value.password

        if (username.isBlank() || password.isBlank()) {
            onError("Email dan password tidak boleh kosong")
            return
        }

        viewModelScope.launch {
            val isLoggedIn = authRepository.login(username, password)
            if (isLoggedIn) {

                val user = authRepository.findUserByEmail(username)

                if (user != null) {
                    SessionManager.loggedInUserEmail = user.email
                    SessionManager.loggedInUserRole = user.role
                    onSuccess(user.email)
                } else {
                    onError("Gagal mendapatkan detail pengguna setelah login.")
                }
            } else {
                onError("Email atau password salah")
            }
        }
    }
}
