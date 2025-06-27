package com.example.projecthmti.ui.theme.Screen.Schedule

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.projecthmti.domain.model.ScheduleItem
import com.example.projecthmti.domain.usecase.AddScheduleUseCase
import com.example.projecthmti.domain.usecase.DeleteScheduleUseCase
import com.example.projecthmti.domain.usecase.EditScheduleUseCase
import com.example.projecthmti.domain.usecase.GetSchedulesUseCase
import com.example.projecthmti.workers.NotificationWorker
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

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
        observeSchedules()
    }

    private fun observeSchedules() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getSchedulesUseCase()
                .catch { e ->
                    _uiState.update { it.copy(errorMessage = e.message, isLoading = false) }
                }
                .collect { scheduleList ->
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

    fun onSaveSchedule(title: String, pelaksana: String, ruang: String, tanggal: Long, context: Context) {
        viewModelScope.launch {
            try {
                val scheduleToSave = _uiState.value.editingSchedule?.copy(
                    id = _uiState.value.editingSchedule!!.id,
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
                    editScheduleUseCase(scheduleToSave)
                } else {
                    addScheduleUseCase(scheduleToSave)
                }

                scheduleReminder(scheduleToSave, context)

                onDialogDismiss()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message) }
            }
        }
    }

    fun deleteSchedule(schedule: ScheduleItem, context: Context) {
        viewModelScope.launch {
            try {
                deleteScheduleUseCase(schedule)
                cancelReminder(schedule, context)
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message) }
            }
        }
    }

    private fun scheduleReminder(schedule: ScheduleItem, context: Context) {
        val workManager = WorkManager.getInstance(context)
        val data = workDataOf(NotificationWorker.KEY_SCHEDULE_TITLE to schedule.title)

        // --- KUNCI PERUBAHAN ---
        // Waktu pengingat sekarang adalah waktu pelaksanaan jadwal itu sendiri.
        val reminderTime = schedule.tanggalPelaksanaan
        // Hitung jeda dari sekarang sampai waktu pelaksanaan.
        val delay = reminderTime - System.currentTimeMillis()

        if (delay > 0) {
            val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(data)
                .build()

            val workName = "reminder_${schedule.id}"

            workManager.enqueueUniqueWork(
                workName,
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
        }
    }

    private fun cancelReminder(schedule: ScheduleItem, context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork("reminder_${schedule.id}")
    }
}
