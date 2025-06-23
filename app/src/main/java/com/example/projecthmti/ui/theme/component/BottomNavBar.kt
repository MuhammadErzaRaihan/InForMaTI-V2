package com.example.projecthmti.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.automirrored.filled.ExitToApp

@Composable
fun BottomNavBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    val items = listOf(
        Icons.Default.Home,           // index 0
        Icons.Default.Group,          // index 1
        null,                         // index 2 (FloatingActionButton)
        Icons.Default.Notifications, // index 3 → NOTIFIKASI
        Icons.AutoMirrored.Filled.ExitToApp // index 4
    )

    Box {
        BottomAppBar(
            containerColor = Color.White,
            contentColor = Color.Gray,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),

            ) {
            items.forEachIndexed { index, icon ->
                if (index == 2) {
                    Spacer(modifier = Modifier.weight(1f))
                } else {
                    IconButton(
                        onClick = { onItemSelected(index) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = icon!!,
                            contentDescription = null,
                            tint = if (index == selectedIndex) Color(0xFF3FD0FF) else Color.Gray
                        )
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = { onItemSelected(2) },
            containerColor = Color(0xFF3FD0FF),
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-20).dp)
                .size(56.dp)
        ) {
            Icon(imageVector = Icons.AutoMirrored.Filled.Chat, contentDescription = "Forum")
        }
    }
}

