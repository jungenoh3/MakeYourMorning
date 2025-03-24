package com.yesnoheun3.makeyourmorning

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.yesnoheun3.makeyourmorning.pages.timesetting.TimeSetting
import com.yesnoheun3.makeyourmorning.pages.TaskRecord
import com.yesnoheun3.makeyourmorning.pages.User

@SuppressLint("LaunchDuringComposition")
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val navItemList = listOf(
        NavItem("TimeSetting", Icons.Rounded.Notifications),
        NavItem("TaskRecord", Icons.Rounded.Create),
        NavItem("User", Icons.Rounded.Person)
    )

    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // 권한 허용됨
            System.out.println("알림 권한 허용됨")
        } else {
            // 권한 거부됨
            System.out.println("알림 권한 거부됨")
        }
    }
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // API 33+
            val isPermissionGranted = ActivityCompat.checkSelfPermission(
                context, "android.permission.POST_NOTIFICATIONS"
            ) == PackageManager.PERMISSION_GRANTED

            if (!isPermissionGranted) {
                requestPermissionLauncher.launch("android.permission.POST_NOTIFICATIONS")
            }
        }
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                navItemList.forEachIndexed{ index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                                  selectedIndex = index
                        },
                        icon = { Icon(imageVector = navItem.icon, contentDescription = "Icon") },
                        label ={
                            Text(text = navItem.label)
                        }
                    )
                }
            }
        }
    ) {innerPadding ->
        ContentScreen(modifier = Modifier.padding(innerPadding), selectedIndex)
    }
}

@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex : Int) {
    System.out.println("selectedIndex: $selectedIndex")

    when(selectedIndex){
        0-> TimeSetting(modifier)
        1-> TaskRecord()
        2-> User()
    }
}