package com.example.projecthmti.ui.theme.screen.member

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecthmti.data.local.db.dao.UserDao
import com.example.projecthmti.domain.model.User
import com.example.projecthmti.util.SessionManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class Member(
    val id: Int,
    val name: String,
    val nim: String,
    val profilePictureUri: String?,
    val isOnline: Boolean
)

class MemberViewModel(userDao: UserDao) : ViewModel() {

    private val loggedInUserEmail = SessionManager.loggedInUserEmail

    val members: StateFlow<List<Member>> = userDao.getAllUsers()
        .map { userEntities ->
            userEntities.map { user ->
                Member(
                    id = user.id,
                    name = user.name,
                    nim = user.nim,
                    profilePictureUri = user.profilePictureUri,
                    isOnline = user.email == loggedInUserEmail
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
