package com.example.projecthmti.ui.theme.Screen.Register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projecthmti.domain.repository.AuthRepository
import com.example.projecthmti.domain.usecase.RegisterUserUseCase


class RegistViewModelFactory(
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegistViewModel::class.java)) {
            val useCase = RegisterUserUseCase(authRepository)

            @Suppress("UNCHECKED_CAST")
            return RegistViewModel(useCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}