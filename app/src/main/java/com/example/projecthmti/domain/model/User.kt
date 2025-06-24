package com.example.projecthmti.domain.model

data class User(
    val id: Int = 0,
    val name: String,
    val nim: String,
    val dob: Long,
    val gender: String,
    val email: String,
    val password: String
)