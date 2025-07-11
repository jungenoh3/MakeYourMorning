package com.nochunsam.makeyourmorning.pages.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.nochunsam.makeyourmorning.pages.main.compose.NavigationGraph
import com.nochunsam.makeyourmorning.ui.theme.MakeYourMorningTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MakeYourMorningTheme {
                val context = LocalContext.current

                var hasNotificationPermission by remember {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        mutableStateOf(
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            )
                                    == PackageManager.PERMISSION_GRANTED
                        )
                    } else mutableStateOf(true)
                }

                var hasOverlayPermission by remember {
                    mutableStateOf(
                        Settings.canDrawOverlays(context)
                    )
                }

                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted ->
                        hasNotificationPermission = isGranted
                    }
                )
                if (!hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    LaunchedEffect(Unit) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }

//                if (!hasOverlayPermission) {
//                    LaunchedEffect(Unit) {
//                        val intent = Intent(
//                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                            "package:${context.packageName}".toUri()
//                        )
//                        context.startActivity(intent)
//                    }
//                }

                System.out.println("notification permission Granted: $hasNotificationPermission")
//                System.out.println("overlay permission Granted: $hasNotificationPermission")

                NavigationGraph("main")
            }
        }
    }
}