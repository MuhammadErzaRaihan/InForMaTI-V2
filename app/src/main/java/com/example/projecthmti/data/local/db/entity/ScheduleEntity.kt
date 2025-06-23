package com.example.projecthmti.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
// ... imports
import com.example.projecthmti.domain.model.ScheduleItem

@Entity(tableName = "schedules")
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val pelaksana: String,
    val ruang: String,
    val tanggalPelaksanaan: Long // Tambahkan field ini
)

// Ubah fungsi mapper
fun ScheduleEntity.toDomainModel(): ScheduleItem {
    return ScheduleItem(
        id = this.id,
        title = this.title,
        pelaksana = this.pelaksana,
        ruang = this.ruang,
        tanggalPelaksanaan = this.tanggalPelaksanaan
    )
}

fun ScheduleItem.toEntity(): ScheduleEntity {
    return ScheduleEntity(
        id = this.id,
        title = this.title,
        pelaksana = this.pelaksana,
        ruang = this.ruang,
        tanggalPelaksanaan = this.tanggalPelaksanaan
    )
}