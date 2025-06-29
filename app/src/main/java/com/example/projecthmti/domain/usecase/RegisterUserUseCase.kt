package com.example.projecthmti.domain.usecase

import com.example.projecthmti.domain.model.User
import com.example.projecthmti.domain.repository.AuthRepository

/**
 * Use case ini menangani satu logika bisnis: mendaftarkan pengguna.
 */
class RegisterUserUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(user: User) {
        authRepository.register(user)
    }
}
