package com.example.projecthmti.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.projecthmti.domain.model.User

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val nim: String,
    val dob: Long,
    val gender: String,
    val email: String,
    val password: String,
    val profilePictureUri: String? = null
)

// Mapper
fun UserEntity.toDomainModel(): User = User(id, name, nim, dob, gender, email, password)
fun User.toEntity(): UserEntity = UserEntity(id, name, nim, dob, gender, email, password)