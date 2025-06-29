package com.example.projecthmti.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.layout.*
import androidx.glance.material3.ColorProviders
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.projecthmti.data.local.db.AppDatabase
import com.example.projecthmti.domain.model.ScheduleItem
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class UpcomingScheduleWidget : GlanceAppWidget() {

    fun update(context: Context) {
        MainScope().launch {
            val manager = GlanceAppWidgetManager(context)
            val glanceIds = manager.getGlanceIds(this@UpcomingScheduleWidget::class.java)
            glanceIds.forEach { glanceId ->
                update(context, glanceId)
            }
        }
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val scheduleDao = AppDatabase.getDatabase(context).scheduleDao()

        // Tentukan awal dan akhir hari ini
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val startOfDay = calendar.timeInMillis

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        val endOfDay = calendar.timeInMillis

        // Ambil semua jadwal untuk hari ini
        val todaysSchedules = scheduleDao.getSchedulesForDay(startOfDay, endOfDay)
            .firstOrNull() ?: emptyList()

        provideContent {
            GlanceTheme{
                WidgetContent(schedules = todaysSchedules)

            }
        }
    }

    @Composable
    private fun WidgetContent(schedules: List<ScheduleItem>) {
        val calendar = Calendar.getInstance()
        val dayOfWeek = SimpleDateFormat("EEEE", Locale("id", "ID")).format(calendar.time).uppercase()
        val dayOfMonth = SimpleDateFormat("d", Locale("id", "ID")).format(calendar.time)

        Row(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(GlanceTheme.colors.surface)
                .cornerRadius(16.dp)
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
                modifier = GlanceModifier.width(60.dp)
            ) {
                Text(
                    text = dayOfWeek.substring(0, 3),
                    style = TextStyle(
                        color = androidx.glance.unit.ColorProvider(Color(0xFF00C7FF)),                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                )
                Text(
                    text = dayOfMonth,
                    style = TextStyle(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = GlanceTheme.colors.onSurface                    )
                )
            }

            Spacer(GlanceModifier.width(12.dp))
            Box(
                modifier = GlanceModifier
                    .fillMaxHeight()
                    .width(2.dp)
                    .background(GlanceTheme.colors.surfaceVariant)
            ) {}
            Spacer(GlanceModifier.width(12.dp))


            if (schedules.isEmpty()) {
                Box(modifier = GlanceModifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Tidak ada jadwal hari ini", style = TextStyle(color = androidx.glance.unit.ColorProvider(
                        Color.Gray
                    )
                    ))
                }
            } else {
                Column(
                    modifier = GlanceModifier.fillMaxHeight(),
                    verticalAlignment = Alignment.Vertical.CenterVertically
                ) {
                    schedules.take(3).forEachIndexed { index, schedule ->
                        ScheduleItemRow(schedule)
                        if (index < schedules.size - 1 && index < 2) {
                            Spacer(GlanceModifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ScheduleItemRow(schedule: ScheduleItem) {
        val timeFormatter = SimpleDateFormat("HH:mm", Locale("id", "ID"))
        val startTime = timeFormatter.format(Date(schedule.tanggalPelaksanaan))

        Row(
            verticalAlignment = Alignment.Vertical.CenterVertically,
            modifier = GlanceModifier.fillMaxWidth()
        ) {
            Box(
                modifier = GlanceModifier
                    .width(4.dp)
                    .height(30.dp)
                    .background(androidx.glance.unit.ColorProvider(Color(0xFF00C7FF))) // Aksen biru
                    .cornerRadius(4.dp)
            ) {}
            Spacer(GlanceModifier.width(8.dp))
            Column {
                Text(
                    text = schedule.title,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = GlanceTheme.colors.onSurface
                    )
                )
                Text(
                    text = "$startTime - ${schedule.ruang}",
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = GlanceTheme.colors.onSurfaceVariant
                    )
                )
            }
        }
    }
}
