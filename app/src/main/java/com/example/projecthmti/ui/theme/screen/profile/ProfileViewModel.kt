package com.example.projecthmti.ui.theme.screen.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecthmti.data.local.db.dao.UserDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class ProfileUiState(
    val name: String = "Loading...",
    val nim: String = "",
    val divisi: String = "",
    val dob: String = "",
    val email: String = "",
    val profilePictureUri: String? = null
)

class ProfileViewModel(
    private val userDao: UserDao,
    private val userEmail: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            val user = userDao.findUserByEmail(userEmail)
            user?.let {
                _uiState.value = ProfileUiState(
                    name = it.name,
                    nim = it.nim,
                    divisi = "PSDM",
                    dob = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date(it.dob)),
                    email = it.email,
                    profilePictureUri = it.profilePictureUri
                )
            }
        }
    }

    fun updateProfilePicture(uri: Uri, context: Context) {
        viewModelScope.launch {
            if (uri.toString().startsWith("content://")) {
                try {
                    context.contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (e: SecurityException) {
                    e.printStackTrace()
                }
            }
            val user = userDao.findUserByEmail(userEmail)
            user?.let {
                val updatedUser = it.copy(profilePictureUri = uri.toString())
                userDao.updateUser(updatedUser)
                loadUserProfile()
            }
        }
    }

    fun getInitials(name: String): String {
        if (name.isBlank() || name == "Loading...") return ""
        return name.split(' ')
            .mapNotNull { it.firstOrNull()?.toString() }
            .take(2)
            .joinToString("")
            .uppercase()
    }
}
