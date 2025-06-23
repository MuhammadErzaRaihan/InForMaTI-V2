package com.example.projecthmti.domain.model

data class ScheduleItem(
    val id: Int = 0, // ID dari database
    val title: String,
    val pelaksana: String,
    val ruang: String,
    val tanggalPelaksanaan: Long // Menyimpan tanggal dan jam
)