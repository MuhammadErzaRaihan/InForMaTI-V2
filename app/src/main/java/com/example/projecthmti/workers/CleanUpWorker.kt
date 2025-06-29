package com.example.projecthmti.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.projecthmti.data.local.db.AppDatabase
import com.example.projecthmti.widget.UpcomingScheduleWidget
import java.util.concurrent.TimeUnit

class CleanupWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val scheduleDao = AppDatabase.getDatabase(applicationContext).scheduleDao()
        return try {
            val oneHourInMillis = TimeUnit.HOURS.toMillis(1)
            val cutoffTime = System.currentTimeMillis() - oneHourInMillis

            scheduleDao.deleteSchedulesOlderThan(cutoffTime)

            UpcomingScheduleWidget().update(applicationContext)

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    companion object {
        const val WORK_NAME = "schedule_cleanup_work"
    }
}
