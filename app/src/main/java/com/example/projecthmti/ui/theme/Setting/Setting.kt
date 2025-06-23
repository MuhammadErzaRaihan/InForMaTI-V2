package com.example.projecthmti.ui.theme.Setting

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.ui.tooling.preview.Preview
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
import com.example.projecthmti.R
import java.util.Locale
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

data class AppSettings(
    val isDarkMode: Boolean = false,
    val isIndonesianLanguage: Boolean = true
)



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingScreenPreview() {
    MaterialTheme {
        SettingScreen(
            onBackClick = {},

        )
    }
}



@Composable
fun SettingScreen(
    onBackClick: () -> Unit,
    settingsViewModel: SettingsViewModel = viewModel()


) {
    val uiState by settingsViewModel.uiState.collectAsState()
    val context = LocalContext.current

    fun changeLanguage(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // back
        IconButton(onClick = { onBackClick() }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }

        // Ganti Bahasa
        SettingItem(
            title = stringResource(R.string.language),
            value = if (uiState.isIndonesianLanguage) stringResource(R.string.indonesian)
            else stringResource(R.string.english),
            icon = Icons.Default.Language,
            onClick = {
                val newLangIsIndonesian = !uiState.isIndonesianLanguage
                settingsViewModel.onLanguageChanged(newLangIsIndonesian)
                changeLanguage(if (newLangIsIndonesian) "in" else "en")
            }
        )

        //  dark mode
        SettingItem(
            title = stringResource(R.string.theme),
            icon = Icons.Default.DarkMode,
            isToggleable = true,
            toggleState = uiState.isDarkMode,
            onToggleChange = { isChecked ->
                settingsViewModel.onThemeChanged(isChecked)
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

