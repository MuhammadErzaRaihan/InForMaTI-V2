package com.example.projecthmti.ui.theme.Screen.Schedule

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecthmti.R
import com.example.projecthmti.data.local.db.AppDatabase
import com.example.projecthmti.domain.model.ScheduleItem
import com.example.projecthmti.ui.theme.ProjectHMTITheme
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val scheduleViewModel: ScheduleViewModel = viewModel(
        factory = ScheduleViewModelFactory(db.scheduleDao())
    )

    val uiState by scheduleViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Jadwal Kegiatan") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF00C7FF),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = { scheduleViewModel.onAddScheduleClicked() }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add_schedule),
                        tint = Color(0xFF00A9D6)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(uiState.schedules) { schedule ->
                        ScheduleCard(
                            item = schedule,
                            onEditClick = { scheduleViewModel.onEditScheduleClicked(schedule) },
                            onDeleteClick = { scheduleViewModel.deleteSchedule(schedule, context) }
                        )
                    }
                }
            }
        }
    }

    if (uiState.isDialogShown) {
        ScheduleInputDialog(
            schedule = uiState.editingSchedule,
            onDismiss = { scheduleViewModel.onDialogDismiss() },
            onSave = { title, pelaksana, ruang, tanggal ->
                scheduleViewModel.onSaveSchedule(title, pelaksana, ruang, tanggal, context)
            }
        )
    }
}

@Composable
fun ScheduleCard(
    item: ScheduleItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    // --- KUNCI PERBAIKAN ADA DI SINI ---
    // Mengganti "Yelp" dengan "yyyy" yang merupakan pola format tahun yang benar.
    val formattedDate = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())
        .format(Date(item.tanggalPelaksanaan))
    // --- SAMPAI SINI ---

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = item.title,
                    color = Color(0xFF00A9D6),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                InfoRow(label = stringResource(R.string.executor), value = item.pelaksana.uppercase())
                InfoRow(label = stringResource(R.string.room), value = item.ruang)
                InfoRow(label = stringResource(R.string.time).uppercase(), value = formattedDate)
            }
            Column {
                IconButton(onClick = onEditClick) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(2f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleInputDialog(
    schedule: ScheduleItem?,
    onDismiss: () -> Unit,
    onSave: (title: String, pelaksana: String, ruang: String, tanggal: Long) -> Unit
) {
    var title by remember { mutableStateOf(schedule?.title ?: "") }
    var pelaksana by remember { mutableStateOf(schedule?.pelaksana ?: "") }

    val roomOptions = listOf("A 13", "A 14", "A 15", "LAB Big Data", "LAB RPL")
    var isRoomDropdownExpanded by remember { mutableStateOf(false) }
    var selectedRoom by remember { mutableStateOf(schedule?.ruang ?: roomOptions[0]) }

    val calendar = Calendar.getInstance()
    if (schedule != null) {
        calendar.timeInMillis = schedule.tanggalPelaksanaan
    }
    var selectedDateTime by remember { mutableStateOf(calendar) }
    val context = LocalContext.current

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour, minute ->
            selectedDateTime.set(Calendar.HOUR_OF_DAY, hour)
            selectedDateTime.set(Calendar.MINUTE, minute)
            selectedDateTime = selectedDateTime.clone() as Calendar
        },
        selectedDateTime.get(Calendar.HOUR_OF_DAY),
        selectedDateTime.get(Calendar.MINUTE),
        true
    )

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            selectedDateTime.set(Calendar.YEAR, year)
            selectedDateTime.set(Calendar.MONTH, month)
            selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            timePickerDialog.show()
        },
        selectedDateTime.get(Calendar.YEAR),
        selectedDateTime.get(Calendar.MONTH),
        selectedDateTime.get(Calendar.DAY_OF_MONTH)
    )

    val formattedDateTime = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())
        .format(selectedDateTime.time)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = if (schedule == null) "Tambah Jadwal" else "Edit Jadwal") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Judul") })
                OutlinedTextField(value = pelaksana, onValueChange = { pelaksana = it }, label = { Text("Pelaksana") })
                ExposedDropdownMenuBox(
                    expanded = isRoomDropdownExpanded,
                    onExpandedChange = { isRoomDropdownExpanded = !isRoomDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedRoom,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Ruang") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isRoomDropdownExpanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = isRoomDropdownExpanded,
                        onDismissRequest = { isRoomDropdownExpanded = false }
                    ) {
                        roomOptions.forEach { room ->
                            DropdownMenuItem(
                                text = { Text(room) },
                                onClick = {
                                    selectedRoom = room
                                    isRoomDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
                OutlinedTextField(
                    value = formattedDateTime,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tanggal & Jam") },
                    trailingIcon = {
                        IconButton(onClick = { datePickerDialog.show() }) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Pilih Tanggal")
                        }
                    }
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSave(title, pelaksana, selectedRoom, selectedDateTime.timeInMillis) }) {
                Text("Simpan")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("Batal") }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ScheduleScreenPreview() {
    ProjectHMTITheme {
        ScheduleScreen(onBackClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun ScheduleCardPreview() {
    ProjectHMTITheme {
        ScheduleCard(
            item = ScheduleItem(
                id = 1,
                title = "RAPAT PARIPURNA",
                pelaksana = "WINDAH BASUDARA",
                ruang = "A-14",
                tanggalPelaksanaan = System.currentTimeMillis()
            ),
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}
