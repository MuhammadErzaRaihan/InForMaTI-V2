package com.example.projecthmti.ui.theme.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.projecthmti.domain.model.ScheduleItem
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ScheduleDetailDialog(
    schedule: ScheduleItem,
    onDismiss: () -> Unit
) {
    // Format tanggal dan waktu agar lebih mudah dibaca
    val formattedDate = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID")).format(Date(schedule.tanggalPelaksanaan))
    val formattedTime = SimpleDateFormat("HH:mm", Locale("id", "ID")).format(Date(schedule.tanggalPelaksanaan))

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = schedule.title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                DetailRow(icon = Icons.Default.Event, label = "Waktu", value = "$formattedDate, Pukul $formattedTime WIB")
                DetailRow(icon = Icons.Default.Person, label = "Pelaksana", value = schedule.pelaksana.uppercase())
                DetailRow(icon = Icons.Default.Place, label = "Ruangan", value = schedule.ruang)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Tutup")
            }
        }
    )
}

@Composable
private fun DetailRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
        }
    }
}
