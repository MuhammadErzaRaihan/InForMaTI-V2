package com.example.projecthmti.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

/**
 * Model data untuk satu pesan chat.
 * Properti dengan nilai default (seperti id dan timestamp) diperlukan agar Firestore
 * bisa membuat objek dari data yang diambil.
 */
data class ChatMessage(
    val id: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val message: String = "",
    @ServerTimestamp
    val timestamp: Date? = null
)
