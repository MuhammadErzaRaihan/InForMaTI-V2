package com.example.projecthmti.domain.repository

import com.example.projecthmti.domain.model.User

/**
 * Interface ini mendefinisikan kontrak untuk semua operasi otentikasi.
 * Metode di sini menggunakan model dari lapisan domain (User).
 */
interface AuthRepository {

    suspend fun login(username: String, password: String): Boolean

    // register sekarang menerima model 'User'
    suspend fun register(user: User)

    suspend fun isEmailRegistered(email: String): Boolean
}
