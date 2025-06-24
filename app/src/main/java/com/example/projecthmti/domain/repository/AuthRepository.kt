package com.example.projecthmti.domain.repository

import com.example.projecthmti.domain.model.User

interface AuthRepository {
    suspend fun login(username: String, password: String): Boolean
    suspend fun register(user: User): Result<Unit>
}