package com.example.projecthmti.data.repository

import com.example.projecthmti.data.local.db.dao.UserDao
import com.example.projecthmti.data.local.db.entity.UserEntity
import com.example.projecthmti.domain.repository.AuthRepository
import com.example.projecthmti.data.local.db.entity.toEntity
import com.example.projecthmti.domain.model.User

/**
 * Implementasi nyata dari AuthRepository yang berinteraksi langsung
 * dengan UserDao untuk mengakses database Room.
 */
class AuthRepositoryImpl(private val userDao: UserDao) : AuthRepository {

    override suspend fun login(username: String, password: String): Boolean {
        val user = userDao.findUserByEmail(username)
        // Login berhasil jika user ditemukan DAN password-nya cocok.
        return user != null && user.password == password
    }

    override suspend fun register(user: User) {
        userDao.registerUser(user.toEntity())
    }

    override suspend fun isEmailRegistered(email: String): Boolean {
        return userDao.isEmailExists(email) > 0
    }

    override suspend fun findUserByEmail(email: String): UserEntity? {
        return userDao.findUserByEmail(email)
    }
}
