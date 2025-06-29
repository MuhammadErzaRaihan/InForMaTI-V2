package com.example.projecthmti.data.local.db.entity

import com.example.projecthmti.domain.model.User

/**
 * Fungsi ini bertugas mengubah objek User (dari domain) menjadi UserEntity (untuk database).
 * Ini adalah bagian penting dari pemisahan antara lapisan data dan domain.
 */
fun User.toEntity(): UserEntity {
    return UserEntity(
        id = this.id,
        name = this.name,
        nim = this.nim,
        dob = this.dob,
        gender = this.gender,
        email = this.email,
        password = this.password,
        profilePictureUri = this.profilePictureUri
    )
}
