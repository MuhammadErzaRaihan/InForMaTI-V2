package com.example.projecthmti.domain.repository

interface AuthRepository {
    suspend fun login(username: String, password: String): Boolean
    // Fungsi-fungsi repositori lainnya
}