package com.example.projecthmti.ui.theme.screen.Home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projecthmti.data.local.db.dao.ScheduleDao
import com.example.projecthmti.data.local.db.dao.UserDao
import com.example.projecthmti.data.repository.ScheduleRepositoryImpl
import com.example.projecthmti.domain.repository.ScheduleRepository
import com.example.projecthmti.domain.usecase.GetSchedulesUseCase

class HomeViewModelFactory(
    private val scheduleDao: ScheduleDao,
    private val userDao: UserDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            val repository: ScheduleRepository = ScheduleRepositoryImpl(scheduleDao)
            val getSchedulesUseCase = GetSchedulesUseCase(repository)

            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(getSchedulesUseCase, userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
