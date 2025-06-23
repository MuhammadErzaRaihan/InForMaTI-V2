package com.example.projecthmti.data.repository

import com.example.projecthmti.data.local.db.dao.ScheduleDao
import com.example.projecthmti.data.local.db.entity.toDomainModel
import com.example.projecthmti.data.local.db.entity.toEntity
import com.example.projecthmti.domain.model.ScheduleItem
import com.example.projecthmti.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ScheduleRepositoryImpl(private val scheduleDao: ScheduleDao) : ScheduleRepository {

    override fun getSchedules(): Flow<List<ScheduleItem>> {
        return scheduleDao.getAllSchedules().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun addSchedule(schedule: ScheduleItem) {
        scheduleDao.insertSchedule(schedule.toEntity())
    }

    override suspend fun editSchedule(schedule: ScheduleItem) {

        scheduleDao.insertSchedule(schedule.toEntity())
    }


    override suspend fun deleteSchedule(schedule: ScheduleItem) {
        scheduleDao.deleteSchedule(schedule.toEntity())
    }
}