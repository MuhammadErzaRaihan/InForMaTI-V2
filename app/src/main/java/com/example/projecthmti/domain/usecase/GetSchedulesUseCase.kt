package com.example.projecthmti.domain.usecase

import com.example.projecthmti.domain.model.ScheduleItem
import com.example.projecthmti.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow

class GetSchedulesUseCase(private val repository: ScheduleRepository) {
    // PERBAIKI FUNGSI INI
    // 1. Hapus 'suspend' karena repository sekarang mengembalikan Flow
    // 2. Ubah return type menjadi Flow<List<ScheduleItem>>
    // 3. Hapus .sortedBy{ it.jam } yang sudah tidak relevan
    operator fun invoke(): Flow<List<ScheduleItem>> {
        return repository.getSchedules()
    }
}