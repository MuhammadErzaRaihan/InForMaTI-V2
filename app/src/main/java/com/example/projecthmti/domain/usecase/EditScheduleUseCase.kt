package com.example.projecthmti.domain.usecase

import com.example.projecthmti.domain.model.ScheduleItem
import com.example.projecthmti.domain.repository.ScheduleRepository

class EditScheduleUseCase(private val repository: ScheduleRepository) {
    suspend operator fun invoke(schedule: ScheduleItem) {
        // Logika bisnis untuk proses edit
        repository.editSchedule(schedule)
    }
}