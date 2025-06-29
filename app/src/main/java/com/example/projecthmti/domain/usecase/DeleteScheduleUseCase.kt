package com.example.projecthmti.domain.usecase

import com.example.projecthmti.domain.model.ScheduleItem // <-- Import ScheduleItem
import com.example.projecthmti.domain.repository.ScheduleRepository

class DeleteScheduleUseCase(private val repository: ScheduleRepository) {
    suspend operator fun invoke(schedule: ScheduleItem) {
        repository.deleteSchedule(schedule)
    }
}