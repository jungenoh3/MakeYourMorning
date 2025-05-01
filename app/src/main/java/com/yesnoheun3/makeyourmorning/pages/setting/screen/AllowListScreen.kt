package com.yesnoheun3.makeyourmorning.pages.setting.screen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yesnoheun3.makeyourmorning.pages.setting.data.InstalledApp
import com.yesnoheun3.makeyourmorning.pages.setting.data.InstalledAppViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun getInstalledApps(context: Context): List<InstalledApp> {
    val pm = context.packageManager

    val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
    val resolveInfos = pm.queryIntentActivities(intent, 0)

    return resolveInfos
        .filter { it.activityInfo.packageName != "com.yesnoheun3.makeyourmorning" }
        .map {
        val appName = it.loadLabel(pm).toString()
        val packageName = it.activityInfo.packageName

        InstalledApp(
            appName = appName,
            packageName = packageName
        )
    }.sortedBy { it.appName.lowercase() }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllowListScreen() {
    val context = LocalContext.current
    val items = remember { mutableStateListOf<InstalledApp>() }
    val viewModel: InstalledAppViewModel = viewModel()
    val allowedList = viewModel.items.collectAsState()

    LaunchedEffect(Unit) {
        val installedApps = withContext(Dispatchers.IO) {
            getInstalledApps(context)
        }
        items.clear()
        items.addAll(installedApps)
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("앱 허용 리스트") }
            )
        }
    ) { paddingValue ->
        LazyColumn(
            modifier = Modifier.padding(paddingValue)
        ) {
            items(items.size) { index ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val check = items[index].packageName in allowedList.value
                            if (check) {
                                viewModel.deleteItem(items[index])
                            } else {
                                viewModel.addItem(items[index])
                            }
                        }
                        .padding(vertical = 15.dp, horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = items[index].appName, fontSize = 20.sp)
                    Checkbox(
                        checked = items[index].packageName in allowedList.value,
                        onCheckedChange = null,
                    )
                }
                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp
                )
            }
        }
    }
}