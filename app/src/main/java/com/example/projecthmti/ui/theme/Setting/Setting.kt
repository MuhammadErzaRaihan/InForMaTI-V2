package com.example.projecthmti.ui.theme.Setting

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecthmti.R
import com.example.projecthmti.data.repository.SettingsRepository

@Composable
fun SettingScreen(
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current
    val settingsRepository = remember { SettingsRepository(context) }
    val settingsViewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(settingsRepository)
    )

    // Observasi uiState tunggal dari ViewModel
    val uiState by settingsViewModel.uiState.collectAsState()

    // Dapatkan referensi ke Activity saat ini untuk memanggil recreate()
    val activity = LocalContext.current as? Activity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        IconButton(onClick = { onBackClick() }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Ganti Bahasa
        SettingItem(
            title = stringResource(R.string.language),
            value = if (uiState.isIndonesianLanguage) stringResource(R.string.indonesian) else stringResource(R.string.english),
            icon = Icons.Default.Language,
            onClick = {
                val newLangIsIndonesian = !uiState.isIndonesianLanguage
                settingsViewModel.onLanguageChanged(newLangIsIndonesian)
                // --- KUNCI PERBAIKAN ---
                // Memicu restart Activity untuk menerapkan perubahan bahasa
                activity?.recreate()
            }
        )

        //  Dark Mode
        SettingItem(
            title = stringResource(R.string.theme),
            icon = Icons.Default.DarkMode,
            isToggleable = true,
            toggleState = uiState.isDarkMode,
            onToggleChange = { isChecked ->
                settingsViewModel.onThemeChanged(isChecked)
                // --- KUNCI PERBAIKAN ---
                // Memicu restart Activity untuk menerapkan perubahan tema juga
                activity?.recreate()
            }
        )
    }
}

@Composable
fun SettingItem(
    title: String,
    value: String? = null,
    icon: ImageVector,
    onClick: (() -> Unit)? = null,
    isToggleable: Boolean = false,
    toggleState: Boolean = false,
    onToggleChange: ((Boolean) -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable(enabled = onClick != null) { onClick?.invoke() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium)
        }

        if (isToggleable && onToggleChange != null) {
            Switch(
                checked = toggleState,
                onCheckedChange = onToggleChange
            )
        } else if (value != null) {
            Text(text = value, fontSize = 16.sp)
        }
    }
}
