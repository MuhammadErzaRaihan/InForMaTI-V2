package com.example.projecthmti.data.local.db.dao

import androidx.room.*
import com.example.projecthmti.data.local.db.entity.ScheduleEntity
import kotlinx.coroutines.flow.Flow
import com.example.projecthmti.domain.model.ScheduleItem

@Dao
interface ScheduleDao {

    @Query("SELECT * FROM schedules ORDER BY id DESC")
    fun getAllSchedules(): Flow<List<ScheduleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: ScheduleEntity)

    @Delete
    suspend fun deleteSchedule(schedule: ScheduleEntity)

    @Query("SELECT * FROM schedules WHERE tanggalPelaksanaan > :currentTime ORDER BY tanggalPelaksanaan ASC")
    fun getUpcomingSchedules(currentTime: Long): Flow<List<ScheduleItem>>

    @Query("SELECT * FROM schedules WHERE tanggalPelaksanaan BETWEEN :startOfDay AND :endOfDay ORDER BY tanggalPelaksanaan ASC")
    fun getSchedulesForDay(startOfDay: Long, endOfDay: Long): Flow<List<ScheduleItem>>

    @Query("DELETE FROM schedules WHERE tanggalPelaksanaan < :cutoffTime")
    suspend fun deleteSchedulesOlderThan(cutoffTime: Long)

}