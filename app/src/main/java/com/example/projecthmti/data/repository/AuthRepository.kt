package com.example.projecthmti.data.repository


import com.example.projecthmti.domain.model.User
import com.example.projecthmti.domain.repository.AuthRepository

class FakeAuthRepository : AuthRepository {
    // Daftar user palsu di dalam memori
    private val users = mutableListOf<User>()

    override suspend fun login(username: String, password: String): Boolean {
        // Simulasi login dengan data hardcoded atau data dari list palsu
        return (username == "hmti" && password == "123456") ||
                users.any { it.email == username && it.password == password }
    }

    override suspend fun register(user: User): Result<Unit> {
        // Simulasi registrasi dengan hanya menambahkannya ke list di memori
        if (users.any { it.email == user.email }) {
            return Result.failure(Exception("Email sudah terdaftar."))
        }
        users.add(user)
        println("FakeAuthRepo: User ${user.email} registered.")
        return Result.success(Unit)
    }
}