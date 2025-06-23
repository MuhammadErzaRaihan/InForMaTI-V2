package com.example.projecthmti.ui.theme.Screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Data class untuk menampung semua state UI di layar registrasi
data class RegistUiState(
    val name: String = "",
    val nim: String = "",
    val dob: String = "", // Date of Birth
    val gender: String = "",
    val email: String = "",
    val password: String = ""
)

class RegistViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegistUiState())
    val uiState: StateFlow<RegistUiState> = _uiState.asStateFlow()

    fun onNameChange(newName: String) {
        _uiState.update { it.copy(name = newName) }
    }

    fun onNimChange(newNim: String) {
        _uiState.update { it.copy(nim = newNim) }
    }

    fun onDobChange(newDob: String) {
        _uiState.update { it.copy(dob = newDob) }
    }

    fun onGenderChange(newGender: String) {
        _uiState.update { it.copy(gender = newGender) }
    }

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    fun onRegisterClick() {
        // Di sini logika untuk memvalidasi dan mengirim data pendaftaran
        // ke Repository akan ditempatkan.
        val currentState = _uiState.value
        println("Mencoba mendaftar dengan data: $currentState")
        // Panggil repository.register(currentState)
    }
}