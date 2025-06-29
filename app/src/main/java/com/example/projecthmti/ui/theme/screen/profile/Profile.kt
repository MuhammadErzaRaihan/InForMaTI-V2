package com.example.projecthmti.ui.theme.screen.profile

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.example.projecthmti.R
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetailScreen(
    onBackClick: () -> Unit,
    profileViewModel: ProfileViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by profileViewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    var tempCameraUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            tempCameraUri?.let { uri ->
                profileViewModel.updateProfilePicture(uri, context)
            }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            profileViewModel.updateProfilePicture(it, context)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            val uri = createImageUri(context)
            tempCameraUri = uri
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Izin kamera diperlukan untuk mengambil foto", Toast.LENGTH_SHORT).show()
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Pilih Sumber Gambar") },
            text = { Text("Pilih dari galeri atau ambil foto baru dengan kamera.") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    galleryLauncher.launch("image/*")
                }) { Text("Galeri") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    when (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)) {
                        PackageManager.PERMISSION_GRANTED -> {
                            val uri = createImageUri(context)
                            tempCameraUri = uri
                            cameraLauncher.launch(uri)
                        }
                        else -> {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
                }) { Text("Kamera") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.profile_detail)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF00C7FF),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .clickable { showDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    val initials = profileViewModel.getInitials(uiState.name)
                    if (uiState.profilePictureUri != null) {
                        AsyncImage(
                            model = Uri.parse(uiState.profilePictureUri),
                            contentDescription = stringResource(R.string.profile_picture),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else if (initials.isNotEmpty()) {
                        Text(
                            text = initials,
                            fontSize = 36.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = uiState.name.uppercase(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                if (uiState.nim.isNotEmpty()) {
                    Text(
                        text = "${uiState.nim} - ${uiState.divisi}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Column(
                modifier = Modifier.padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = stringResource(R.string.profile_details_section),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                ProfileDetailItem(stringResource(R.string.name), uiState.name)
                ProfileDetailItem(stringResource(R.string.nim), uiState.nim)
                ProfileDetailItem(stringResource(R.string.dob), uiState.dob)
                ProfileDetailItem(stringResource(R.string.email), uiState.email)
            }
        }
    }
}

private fun createImageUri(context: Context): Uri {
    val cacheDir = context.externalCacheDir ?: context.cacheDir
    val file = File(cacheDir, "camera_photo_${System.currentTimeMillis()}.jpg")

    return FileProvider.getUriForFile(
        context,
        "com.example.projecthmti.provider",
        file
    )
}

@Composable
fun ProfileDetailItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray
        )
        Text(
            text = if (value != "Loading...") value else "",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}
