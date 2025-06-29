package com.example.projecthmti.data.local.db

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.projecthmti.data.local.db.dao.NotificationDao
import com.example.projecthmti.data.local.db.dao.ScheduleDao
import com.example.projecthmti.data.local.db.dao.UserDao
import com.example.projecthmti.data.local.db.entity.ScheduleEntity
import com.example.projecthmti.data.local.db.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.projecthmti.data.local.db.entity.NotificationEntity

@Database(
    entities = [ScheduleEntity::class, UserEntity::class, NotificationEntity::class],
    version = 5,
    exportSchema = true,
    autoMigrations = [
        AutoMigration (from = 1, to = 2),
        AutoMigration (from = 2, to = 3),
        AutoMigration (from = 3, to = 4),
        AutoMigration (from = 4, to = 5)
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun scheduleDao(): ScheduleDao
    abstract fun userDao(): UserDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "hmti_database"
                )
                    // Pastikan untuk menambahkan callback di sini
                    .addCallback(DatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    // Callback untuk mengisi data awal
    private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateUsers(database.userDao())
                }
            }
        }

        suspend fun populateUsers(userDao: UserDao) {
            val hmtiUser = UserEntity(
                name = "Muhammad Erza Raihan",
                nim = "2310817210027/H1G115222",
                dob = 946684800000L,
                gender = "Laki-laki",
                email = "hmti",
                password = "123456",
                role = "ADMIN"
            )
            userDao.registerUser(hmtiUser)
        }
    }
}