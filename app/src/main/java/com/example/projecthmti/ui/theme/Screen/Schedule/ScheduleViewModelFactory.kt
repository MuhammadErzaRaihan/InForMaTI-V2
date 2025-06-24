package com.example.projecthmti.ui.theme.Screen.Schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projecthmti.data.repository.ScheduleRepositoryImpl
import com.example.projecthmti.domain.repository.ScheduleRepository
import com.example.projecthmti.domain.usecase.AddScheduleUseCase
import com.example.projecthmti.domain.usecase.DeleteScheduleUseCase
import com.example.projecthmti.domain.usecase.EditScheduleUseCase
import com.example.projecthmti.domain.usecase.GetSchedulesUseCase
import com.example.projecthmti.data.local.db.dao.ScheduleDao


// Factory ini tahu cara membuat semua dependensi yang dibutuhkan ScheduleViewModel
class ScheduleViewModelFactory(
    private val scheduleDao: ScheduleDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScheduleViewModel::class.java)) {
            // Buat repository di sini, dengan DAO yang sudah disediakan
            val repository: ScheduleRepository = ScheduleRepositoryImpl(scheduleDao)
            @Suppress("UNCHECKED_CAST")
            return ScheduleViewModel(
                getSchedulesUseCase = GetSchedulesUseCase(repository),
                addScheduleUseCase = AddScheduleUseCase(repository),
                editScheduleUseCase = EditScheduleUseCase(repository),
                deleteScheduleUseCase = DeleteScheduleUseCase(repository)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}