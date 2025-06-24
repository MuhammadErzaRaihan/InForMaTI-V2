package com.example.projecthmti.ui.theme.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.example.projecthmti.R
import com.example.projecthmti.domain.model.ScheduleItem
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun UpcomingEventSection(
    schedules: List<ScheduleItem>,
    onAddScheduleClick: () -> Unit = {}
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Text(
                text = stringResource(R.string.Schedule),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = onAddScheduleClick,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Schedule",
                    tint = Color(0xFF00C7FF)
                )
            }
        }

        // Cek jika daftar jadwal kosong
        if (schedules.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tidak ada jadwal kegiatan.",
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
            }
        } else {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(schedules) { schedule ->
                    UpcomingScheduleCard(schedule)
                }
            }
        }
    }
}

@Composable
fun UpcomingScheduleCard(schedule: ScheduleItem) {
    // Ambil hanya jam dari timestamp
    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    val timeString = timeFormatter.format(Date(schedule.tanggalPelaksanaan))

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .width(220.dp)
            .height(100.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .border(2.dp, Color(0xFF3FD0FF), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = timeString, // Tampilkan jam
                    color = Color(0xFF3FD0FF),
                    fontWeight = FontWeight.Bold
                )
            }
            Column {
                Text(
                    text = schedule.title,
                    color = Color(0xFF3FD0FF),
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = "Location",
                        tint = Color.Black,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = schedule.ruang, color = Color.Black)
                }
            }
        }
    }
}

// data class Event sudah tidak diperlukan lagi, bisa dihapus.