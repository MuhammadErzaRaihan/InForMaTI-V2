package com.example.projecthmti.domain.usecase

import com.example.projecthmti.domain.model.ScheduleItem // <-- Import ScheduleItem
import com.example.projecthmti.domain.repository.ScheduleRepository

class DeleteScheduleUseCase(private val repository: ScheduleRepository) {
    // UBAH PARAMETER DARI scheduleId: String MENJADI schedule: ScheduleItem
    suspend operator fun invoke(schedule: ScheduleItem) {
        // Logika bisnis untuk proses hapus bisa ditambahkan di sini jika perlu
        repository.deleteSchedule(schedule) // Sekarang tipe datanya sudah cocok
    }
}