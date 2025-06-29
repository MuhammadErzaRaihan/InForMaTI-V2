package com.example.projecthmti.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.projecthmti.R
import com.example.projecthmti.data.local.db.AppDatabase
import com.example.projecthmti.data.local.db.entity.NotificationEntity

class NotificationWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val scheduleTitle = inputData.getString(KEY_SCHEDULE_TITLE) ?: return Result.failure()
        val notificationType = inputData.getString(KEY_NOTIFICATION_TYPE) ?: return Result.failure()

        val message = when (notificationType) {
            TYPE_30_MIN_REMINDER -> "Acara '$scheduleTitle' akan dilaksanakan 30 menit lagi."
            TYPE_ON_TIME_REMINDER -> "Acara '$scheduleTitle' sedang berlangsung sekarang."
            else -> return Result.failure()
        }


        sendNotification("Pengingat Jadwal", message)

        val notificationDao = AppDatabase.getDatabase(context).notificationDao()
        notificationDao.insertNotification(
            NotificationEntity(
                message = message,
                timestamp = System.currentTimeMillis()
            )
        )

        return Result.success()
    }

    private fun sendNotification(title: String, message: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Pengingat Jadwal",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifikasi untuk jadwal kegiatan."
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo_hmti)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    companion object {
        const val KEY_SCHEDULE_TITLE = "key_schedule_title"
        const val KEY_NOTIFICATION_TYPE = "key_notification_type"

        const val TYPE_30_MIN_REMINDER = "type_30_min_reminder"
        const val TYPE_ON_TIME_REMINDER = "type_on_time_reminder"

        const val CHANNEL_ID = "schedule_reminder_channel"
    }
}
