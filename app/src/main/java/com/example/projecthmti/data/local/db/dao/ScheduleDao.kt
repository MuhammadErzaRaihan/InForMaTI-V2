package com.example.projecthmti.data.local.db.dao

import androidx.room.*
import com.example.projecthmti.data.local.db.entity.ScheduleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    // Mengambil semua jadwal dan mengembalikannya sebagai Flow.
    // Flow akan otomatis mengirim data baru jika ada perubahan di tabel.
    @Query("SELECT * FROM schedules ORDER BY id DESC")
    fun getAllSchedules(): Flow<List<ScheduleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: ScheduleEntity)

    @Delete
    suspend fun deleteSchedule(schedule: ScheduleEntity)
}