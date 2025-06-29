package com.example.projecthmti.domain.repository

import com.example.projecthmti.domain.model.ScheduleItem
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {
    fun getSchedules(): Flow<List<ScheduleItem>>
    suspend fun addSchedule(schedule: ScheduleItem)
    suspend fun editSchedule(schedule: ScheduleItem)
    suspend fun deleteSchedule(schedule: ScheduleItem)
}