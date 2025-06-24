package com.example.projecthmti.data.repository

import com.example.projecthmti.data.local.db.dao.UserDao
import com.example.projecthmti.data.local.db.entity.toEntity
import com.example.projecthmti.domain.model.User
import com.example.projecthmti.domain.repository.AuthRepository

class AuthRepositoryImpl(private val userDao: UserDao) : AuthRepository {
    override suspend fun login(username: String, password: String): Boolean {
        val user = userDao.findUserByEmail(username)
        return user != null && user.password == password
    }

    override suspend fun register(user: User): Result<Unit> {
        return try {
            userDao.registerUser(user.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}