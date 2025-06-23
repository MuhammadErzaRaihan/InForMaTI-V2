package com.example.projecthmti.data.repository

import com.example.projecthmti.domain.repository.AuthRepository

class AuthRepositoryImpl : AuthRepository {
    override suspend fun login(username: String, password: String): Boolean {
        // Logika untuk login ke API atau database
        return username == "hmti" && password == "123456"
    }
}