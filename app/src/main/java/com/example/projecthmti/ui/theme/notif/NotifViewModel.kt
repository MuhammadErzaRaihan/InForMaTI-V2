package com.example.projecthmti.ui.theme.notif

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecthmti.data.local.db.dao.NotificationDao
import com.example.projecthmti.data.local.db.entity.NotificationEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotifViewModel(
    private val notificationDao: NotificationDao // Jadikan private
) : ViewModel() {

    val notifications: StateFlow<List<NotificationEntity>> =
        notificationDao.getAllNotifications()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun deleteNotification(notification: NotificationEntity) {
        viewModelScope.launch {
            notificationDao.deleteNotification(notification)
        }
    }
}
