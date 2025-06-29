package com.example.projecthmti.ui.theme.Screen.Register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projecthmti.domain.repository.AuthRepository
import com.example.projecthmti.domain.usecase.RegisterUserUseCase

/**
 * Factory ini membuat RegistViewModel dan menyediakan dependensi yang dibutuhkan:
 * RegisterUserUseCase dan AuthRepository.
 */
class RegistViewModelFactory(
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegistViewModel::class.java)) {
            val registerUserUseCase = RegisterUserUseCase(authRepository)

            @Suppress("UNCHECKED_CAST")
            return RegistViewModel(registerUserUseCase, authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
