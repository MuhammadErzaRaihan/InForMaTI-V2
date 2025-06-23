package com.example.projecthmti.data.repository

interface AuthRepository {
    suspend fun login(username: String, password: String): Boolean
}

class FakeAuthRepository : AuthRepository {
    override suspend fun login(username: String, password: String): Boolean {
        // Simulasi panggilan ke API atau database
        return username == "hmti" && password == "123456"
    }
}