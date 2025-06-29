package com.example.projecthmti.ui.theme.screen.Register

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecthmti.domain.model.User
import com.example.projecthmti.domain.repository.AuthRepository
import com.example.projecthmti.domain.usecase.RegisterUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// State untuk menampung pesan error per field
data class RegistrationFormErrors(
    val nameError: String? = null,
    val nimError: String? = null,
    val dobError: String? = null,
    val genderError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null
)

// UI State yang diperbarui dengan state error
data class RegistUiState(
    val name: String = "",
    val nim: String = "",
    val dob: Long? = null,
    val gender: String = "",
    val email: String = "",
    val password: String = "",
    val errors: RegistrationFormErrors = RegistrationFormErrors()
)

class RegistViewModel(
    private val registerUserUseCase: RegisterUserUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistUiState())
    val uiState: StateFlow<RegistUiState> = _uiState.asStateFlow()

    fun onNameChange(name: String) { _uiState.update { it.copy(name = name) } }
    fun onNimChange(nim: String) { _uiState.update { it.copy(nim = nim) } }
    fun onDobChange(dob: Long) { _uiState.update { it.copy(dob = dob) } }
    fun onGenderChange(gender: String) { _uiState.update { it.copy(gender = gender) } }
    fun onEmailChange(email: String) { _uiState.update { it.copy(email = email) } }
    fun onPasswordChange(password: String) { _uiState.update { it.copy(password = password) } }

    fun onRegisterClick(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            if (validateForm()) {
                try {
                    val user = User(
                        name = uiState.value.name,
                        nim = uiState.value.nim,
                        dob = uiState.value.dob!!,
                        gender = uiState.value.gender,
                        email = uiState.value.email.lowercase(),
                        password = uiState.value.password
                    )
                    registerUserUseCase(user)
                    onSuccess()
                } catch (e: Exception) {
                    onError("Registrasi gagal: ${e.message}")
                }
            } else {
                onError("Mohon perbaiki semua kesalahan pada formulir.")
            }
        }
    }

    private suspend fun validateForm(): Boolean {
        val state = _uiState.value
        val errors = RegistrationFormErrors(
            nameError = if (state.name.isBlank()) "Nama tidak boleh kosong" else null,
            nimError = if (state.nim.isBlank()) "NIM tidak boleh kosong" else if (!state.nim.all { it.isDigit() }) "NIM hanya boleh berisi angka" else null,
            dobError = if (state.dob == null) "Tanggal lahir harus diisi" else null,
            genderError = if (state.gender.isBlank()) "Jenis kelamin harus dipilih" else null,
            emailError = if (state.email.isBlank()) "Email tidak boleh kosong"
            else if (!Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) "Format email tidak valid"
            else if (authRepository.isEmailRegistered(state.email)) "Email sudah terdaftar"
            else null,
            passwordError = if (state.password.length < 6) "Password minimal 6 karakter" else null
        )

        _uiState.update { it.copy(errors = errors) }

        return errors.nameError == null && errors.nimError == null && errors.dobError == null &&
                errors.genderError == null && errors.emailError == null && errors.passwordError == null
    }
}
