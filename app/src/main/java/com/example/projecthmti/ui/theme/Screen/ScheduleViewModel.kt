package com.example.projecthmti.ui.theme.Screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// ScheduleItem sudah ada di Schedule.kt, kita gunakan lagi di sini

// UI State untuk menampung daftar jadwal
data class ScheduleUiState(
    val schedules: List<ScheduleItem> = emptyList()
)

class ScheduleViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ScheduleUiState())
    val uiState: StateFlow<ScheduleUiState> = _uiState.asStateFlow()

    init {
        // Inisialisasi data jadwal.
        // Nantinya, data ini akan diambil dari Repository.
        loadSchedules()
    }

    private fun loadSchedules() {
        // Data yang sebelumnya hardcoded di UI, sekarang dikelola di ViewModel
        val scheduleList = listOf(
            ScheduleItem("RAPAT PARIPURNA", "WINDAH BASUDARA", "A-14", "08.00 - 10.30"),
            ScheduleItem("RAPAT MBG", "PRAVRORO", "A-14", "08.00 - 10.30"),
            ScheduleItem("RAPAT BOCIL KEMATIAN", "TARA DUGONG", "A-14", "08.00 - 10.30"),
            ScheduleItem("RAPAT SKIBIDI", "JOHNNY G PIRING", "A-14", "08.00 - 10.30"),
            ScheduleItem("RAPAT RIZZ", "ZU KO WI", "A-14", "08.00 - 10.30"),
            ScheduleItem("RAPAT GYATT", "CABUZERO DAGUSQUERO", "A-14", "08.00 - 10.30")
        )
        _uiState.value = ScheduleUiState(schedules = scheduleList)
    }

    fun addSchedule() {
        // Logika untuk menambah jadwal baru
        println("Add schedule clicked")
    }

    fun editSchedule() {
        // Logika untuk mengedit jadwal
        println("Edit schedule clicked")
    }

    fun deleteSchedule() {
        // Logika untuk menghapus jadwal
        println("Delete schedule clicked")
    }
}