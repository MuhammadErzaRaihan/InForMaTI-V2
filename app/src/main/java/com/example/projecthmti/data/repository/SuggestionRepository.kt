package com.example.projecthmti.data.repository

import com.example.projecthmti.data.model.Suggestion
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SuggestionRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val suggestionsCollection = firestore.collection("suggestions")

    suspend fun sendSuggestion(suggestion: Suggestion) {
        suggestionsCollection.add(suggestion).await()
    }
}
