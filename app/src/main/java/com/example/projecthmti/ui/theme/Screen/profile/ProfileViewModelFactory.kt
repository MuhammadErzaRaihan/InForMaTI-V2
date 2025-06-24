package com.example.projecthmti.ui.theme.screen.profile // <-- PERBAIKAN NAMA PAKET

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projecthmti.data.local.db.dao.UserDao
import com.example.projecthmti.util.SessionManager

class ProfileViewModelFactory(
    private val userDao: UserDao,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Karena semua file sekarang ada di paket yang benar, referensi ini akan ditemukan.
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            val email = SessionManager.loggedInUserEmail
                ?: throw IllegalStateException("Email pengguna tidak ditemukan di sesi. Pastikan login berhasil.")

            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(userDao, email) as T
        }
        throw IllegalArgumentException("Kelas ViewModel tidak dikenal: ${modelClass.name}")
    }
}
