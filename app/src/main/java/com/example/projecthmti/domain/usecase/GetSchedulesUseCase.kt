package com.example.projecthmti.domain.usecase

import com.example.projecthmti.domain.model.ScheduleItem
import com.example.projecthmti.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow

class GetSchedulesUseCase(private val repository: ScheduleRepository) {
    operator fun invoke(): Flow<List<ScheduleItem>> {
        return repository.getSchedules()
    }
}