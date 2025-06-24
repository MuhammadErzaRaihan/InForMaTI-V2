package com.example.projecthmti.domain.usecase

import com.example.projecthmti.domain.model.User
import com.example.projecthmti.domain.repository.AuthRepository

class RegisterUserUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(user: User): Result<Unit> {
        // Logika validasi bisa ditambahkan di sini
        if (user.password.length < 6) {
            return Result.failure(Exception("Password minimal 6 karakter."))
        }
        return authRepository.register(user)
    }
}