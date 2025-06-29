package com.example.projecthmti.ui.theme.screen.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.projecthmti.data.local.db.dao.UserDao
import com.example.projecthmti.data.model.ChatMessage
import com.example.projecthmti.data.repository.ChatRepository
import com.example.projecthmti.util.SessionManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val currentMessage: String = ""
)

class ChatViewModel(
    private val chatRepository: ChatRepository,
    private val userDao: UserDao
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private var senderName: String = "Unknown User"

    init {
        loadCurrentUser()
        observeMessages()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            val email = SessionManager.loggedInUserEmail ?: return@launch
            val user = userDao.findUserByEmail(email)
            senderName = user?.name ?: "User"
        }
    }

    private fun observeMessages() {
        viewModelScope.launch {
            chatRepository.getChatMessages().collect { messages ->
                _uiState.update { it.copy(messages = messages) }
            }
        }
    }

    fun onMessageChange(message: String) {
        _uiState.update { it.copy(currentMessage = message) }
    }

    fun sendMessage() {
        val messageText = uiState.value.currentMessage.trim()
        if (messageText.isBlank()) return

        _uiState.update { it.copy(currentMessage = "") }


        val senderId = SessionManager.loggedInUserEmail ?: "anonymous"

        viewModelScope.launch {
            val message = ChatMessage(
                senderId = senderId,
                senderName = senderName,
                message = messageText
            )
            try {
                chatRepository.sendMessage(message)
            } catch (e: Exception) {
                _uiState.update { it.copy(currentMessage = messageText) }
            }
        }
    }
}

class ChatViewModelFactory(
    private val chatRepository: ChatRepository,
    private val userDao: UserDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatViewModel(chatRepository, userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
