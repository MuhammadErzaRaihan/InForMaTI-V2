package com.example.projecthmti.ui.theme.Screen.Schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecthmti.domain.model.ScheduleItem
import com.example.projecthmti.domain.usecase.AddScheduleUseCase
import com.example.projecthmti.domain.usecase.DeleteScheduleUseCase
import com.example.projecthmti.domain.usecase.EditScheduleUseCase
import com.example.projecthmti.domain.usecase.GetSchedulesUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ScheduleUiState(
    val schedules: List<ScheduleItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isDialogShown: Boolean = false,
    val editingSchedule: ScheduleItem? = null
)

class ScheduleViewModel(
    private val getSchedulesUseCase: GetSchedulesUseCase,
    private val addScheduleUseCase: AddScheduleUseCase,
    private val editScheduleUseCase: EditScheduleUseCase,
    private val deleteScheduleUseCase: DeleteScheduleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScheduleUiState())
    val uiState: StateFlow<ScheduleUiState> = _uiState.asStateFlow()

    init {
        // Panggil fungsi untuk mulai mendengarkan perubahan dari database
        observeSchedules()
    }

    // Fungsi ini akan terus berjalan dan mendengarkan perubahan dari Flow
    private fun observeSchedules() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            getSchedulesUseCase() // Panggil use case yang mengembalikan Flow
                .catch { e -> // Tangani error jika terjadi pada Flow
                    _uiState.update { it.copy(errorMessage = e.message, isLoading = false) }
                }
                .collect { scheduleList -> // Kumpulkan data dari Flow
                    _uiState.update {
                        it.copy(schedules = scheduleList, isLoading = false)
                    }
                }
        }
    }

    fun onAddScheduleClicked() {
        _uiState.update { it.copy(isDialogShown = true, editingSchedule = null) }
    }

    fun onEditScheduleClicked(item: ScheduleItem) {
        _uiState.update { it.copy(isDialogShown = true, editingSchedule = item) }
    }

    fun onDialogDismiss() {
        _uiState.update { it.copy(isDialogShown = false, editingSchedule = null) }
    }

    fun onSaveSchedule(title: String, pelaksana: String, ruang: String, tanggal: Long) {
        viewModelScope.launch {
            // Perbaikan: Tambahkan blok try-catch
            try {
                val scheduleToSave = _uiState.value.editingSchedule?.copy(
                    id = _uiState.value.editingSchedule!!.id, // Pastikan ID lama ikut terbawa
                    title = title,
                    pelaksana = pelaksana,
                    ruang = ruang,
                    tanggalPelaksanaan = tanggal
                ) ?: ScheduleItem(
                    title = title,
                    pelaksana = pelaksana,
                    ruang = ruang,
                    tanggalPelaksanaan = tanggal
                )

                if (_uiState.value.editingSchedule != null) {
                    editScheduleUseCase(scheduleToSave) // Hapus komentar
                } else {
                    addScheduleUseCase(scheduleToSave) // Hapus komentar
                }
                onDialogDismiss()
                // Tidak perlu memanggil loadSchedules() lagi, karena Flow akan update otomatis
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message) }
            }
        }
    }

    fun deleteSchedule(schedule: ScheduleItem) { // <-- Menerima ScheduleItem dari UI
        viewModelScope.launch {
            try {
                deleteScheduleUseCase(schedule) // <-- Mengirim ScheduleItem ke use case. Sudah benar.
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message) }
            }
        }
    }
}