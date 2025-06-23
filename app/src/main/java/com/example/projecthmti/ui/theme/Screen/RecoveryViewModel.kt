package com.example.projecthmti.ui.theme.Screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class RecoveryUiState(
    val email: String = "",
    val oldPassword: String = ""
)

class RecoveryViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RecoveryUiState())
    val uiState: StateFlow<RecoveryUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(oldPassword = password) }
    }

    fun onRecoverySubmit() {
        // Logika untuk mengirim permintaan pemulihan akun ke Repository
        val currentState = _uiState.value
        println("Memulai pemulihan untuk email: ${currentState.email}")
        // Panggil repository.recoverAccount(currentState.email, currentState.oldPassword)
    }
}