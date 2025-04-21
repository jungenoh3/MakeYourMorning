package com.yesnoheun3.makeyourmorning

import android.content.pm.PackageManager
import android.os.Bundle
import android.Manifest
import android.content.Intent
import android.os.Build
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
import androidx.core.net.toUri
import com.yesnoheun3.makeyourmorning.common.alarmManage.AlarmService
import com.yesnoheun3.makeyourmorning.ui.theme.MakeYourMorningTheme
import com.yesnoheun3.makeyourmorning.pages.main.MainScreen
import com.yesnoheun3.makeyourmorning.pages.main.NavigationGraph

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
                                Manifest.permission.POST_NOTIFICATIONS)
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