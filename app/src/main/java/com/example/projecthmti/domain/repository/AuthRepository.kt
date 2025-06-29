package com.example.projecthmti.domain.repository

import com.example.projecthmti.data.local.db.entity.UserEntity
import com.example.projecthmti.domain.model.User

/**
 * Interface ini mendefinisikan kontrak untuk semua operasi otentikasi.
 * Metode di sini menggunakan model dari lapisan domain (User).
 */
interface AuthRepository {

    suspend fun login(username: String, password: String): Boolean

    suspend fun register(user: User)

    suspend fun isEmailRegistered(email: String): Boolean

    suspend fun findUserByEmail(email: String): UserEntity?

}
