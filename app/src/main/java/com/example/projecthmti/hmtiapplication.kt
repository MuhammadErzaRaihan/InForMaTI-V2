package com.example.projecthmti

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.projecthmti.workers.CleanupWorker
import java.util.concurrent.TimeUnit

class HmtiApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        scheduleCleanup()
    }

    private fun scheduleCleanup() {
        // Buat permintaan pekerjaan yang akan diulang setiap 12 jam
        val cleanupRequest = PeriodicWorkRequestBuilder<CleanupWorker>(12, TimeUnit.HOURS)
            .build()

        // `KEEP` berarti jika pekerjaan sudah ada, jangan buat yang baru.
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            CleanupWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            cleanupRequest
        )
    }
}
