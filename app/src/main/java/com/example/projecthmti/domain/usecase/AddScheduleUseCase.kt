package com.example.projecthmti.domain.usecase

import com.example.projecthmti.domain.model.ScheduleItem
import com.example.projecthmti.domain.repository.ScheduleRepository

class AddScheduleUseCase(private val repository: ScheduleRepository) {
    suspend operator fun invoke(schedule: ScheduleItem) {
        if (schedule.title.isBlank()) {
            throw IllegalArgumentException("Judul tidak boleh kosong.")
        }
        repository.addSchedule(schedule)
    }
}