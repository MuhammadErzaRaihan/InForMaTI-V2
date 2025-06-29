package com.example.projecthmti.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Suggestion(
    val senderId: String = "",
    val senderName: String = "",
    val message: String = "",
    @ServerTimestamp
    val timestamp: Date? = null
)
