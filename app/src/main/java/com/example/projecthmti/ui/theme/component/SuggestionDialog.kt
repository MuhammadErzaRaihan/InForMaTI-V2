package com.example.projecthmti.ui.theme.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SuggestionDialog(
    onDismiss: () -> Unit,
    onSend: (String) -> Unit
) {
    var message by remember { mutableStateOf("") }
    val isSendEnabled = message.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Kirim Kritik & Saran") },
        text = {
            Column {
                Text("Masukan Anda akan membantu kami menjadi lebih baik. Silakan tulis masukan Anda di bawah ini.")
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Pesan Anda...") },

                    modifier = Modifier.fillMaxWidth().height(150.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSend(message) },
                enabled = isSendEnabled
            ) {
                Text("Kirim")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
