package com.example.projecthmti.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projecthmti.data.local.db.dao.ScheduleDao
import com.example.projecthmti.data.local.db.entity.ScheduleEntity
import androidx.room.AutoMigration

@Database(
    entities = [ScheduleEntity::class],
    version = 1, // <-- NAIKKAN VERSI DARI 1 KE 2
    exportSchema = true,

)


abstract class AppDatabase : RoomDatabase() {

    abstract fun scheduleDao(): ScheduleDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "hmti_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}