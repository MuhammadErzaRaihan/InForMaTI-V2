package com.example.projecthmti.data.local.db.entity

import androidx.room.ColumnInfo
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
    val profilePictureUri: String? = null,
    @ColumnInfo(defaultValue = "MEMBER")
    val role: String = "MEMBER")

