package com.example.projecthmti.data.repository

import com.example.projecthmti.data.model.ChatMessage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ChatRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val messagesCollection = firestore.collection("global_chat")

    // Mengirim pesan baru ke Firestore
    suspend fun sendMessage(message: ChatMessage) {
        messagesCollection.add(message).await()
    }

    // Mendengarkan perubahan pesan secara real-time
    fun getChatMessages(): Flow<List<ChatMessage>> = callbackFlow {
        val subscription = messagesCollection
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val messages = snapshot.toObjects(ChatMessage::class.java)
                    trySend(messages).isSuccess
                }
            }

        // Batalkan listener saat Flow tidak lagi digunakan
        awaitClose { subscription.remove() }
    }
}
