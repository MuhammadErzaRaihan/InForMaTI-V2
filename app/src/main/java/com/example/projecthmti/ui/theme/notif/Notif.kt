package com.example.projecthmti.ui.theme.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.*
import com.example.projecthmti.R
import com.example.projecthmti.data.local.db.AppDatabase
import com.example.projecthmti.data.local.db.entity.NotificationEntity
import com.example.projecthmti.ui.theme.notif.NotifViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotifScreen() {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val notifViewModel: NotifViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return NotifViewModel(db.notificationDao()) as T
        }
    })

    val notifications by notifViewModel.notifications.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.notif),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3FD0FF),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        if (notifications.isEmpty()) {
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillParentMaxSize()
                ) {
                    Text(
                        text = stringResource(R.string.Nonew),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    LottieAnimationView(modifier = Modifier.size(200.dp))
                }
            }
        } else {
            // --- PERUBAHAN UTAMA DI SINI ---
            items(
                items = notifications,
                key = { notification -> notification.id } // Kunci stabil untuk animasi
            ) { notification ->
                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = {
                        // Hanya konfirmasi jika geser ke kiri (EndToStart)
                        if (it == SwipeToDismissBoxValue.EndToStart) {
                            notifViewModel.deleteNotification(notification)
                            true
                        } else {
                            false
                        }
                    }
                )

                SwipeToDismissBox(
                    state = dismissState,
                    // Hanya izinkan geser dari kanan ke kiri
                    enableDismissFromStartToEnd = false,
                    backgroundContent = {
                        // Latar belakang yang muncul saat item digeser
                        val color = when (dismissState.targetValue) {
                            SwipeToDismissBoxValue.EndToStart -> Color.Red.copy(alpha = 0.8f)
                            else -> Color.Transparent
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color)
                                .padding(horizontal = 20.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Hapus Notifikasi",
                                tint = Color.White
                            )
                        }
                    }
                ) {
                    // Konten asli (item notifikasi)
                    NotificationItem(notification)
                }
            }
        }
    }
}

@Composable
fun NotificationItem(notification: NotificationEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = notification.message,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault()).format(Date(notification.timestamp)),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun LottieAnimationView(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("error.json")
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        speed = 1f
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier
    )
}

@Preview(showSystemUi = true)
@Composable
fun NotifScreenPreview() {
    NotifScreen()
}
