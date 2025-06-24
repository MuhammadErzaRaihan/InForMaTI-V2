package com.example.projecthmti.ui.theme.Screen.Register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecthmti.domain.model.User
import com.example.projecthmti.domain.usecase.RegisterUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Data class untuk menampung semua state UI di layar registrasi
data class RegistUiState(
    val name: String = "",
    val nim: String = "",
    val dob: Long? = null, // Tanggal Lahir
    val gender: String = "",
    val email: String = "",
    val password: String = ""
)

// Ini adalah kelas ViewModel yang sebenarnya.
// Perhatikan bahwa kelas Factory tidak ada di sini.
class RegistViewModel(private val registerUserUseCase: RegisterUserUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(RegistUiState())
    val uiState: StateFlow<RegistUiState> = _uiState.asStateFlow()

    fun onNameChange(newName: String) {
        _uiState.update { it.copy(name = newName) }
    }

    fun onNimChange(newNim: String) {
        _uiState.update { it.copy(nim = newNim) }
    }

    fun onDobChange(newDob: Long) {
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

    fun onRegisterClick(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val state = _uiState.value
            if (state.dob == null) {
                onError("Tanggal lahir harus diisi.")
                return@launch
            }
            val user = User(
                name = state.name,
                nim = state.nim,
                dob = state.dob,
                gender = state.gender,
                email = state.email,
                password = state.password
            )

            registerUserUseCase(user)
                .onSuccess { onSuccess() }
                .onFailure { onError(it.message ?: "Terjadi kesalahan") }
        }
    }
}