@file:OptIn(ExperimentalMaterial3Api::class)

package com.yesnoheun3.makeyourmorning.pages.time

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.FloatingActionButton
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yesnoheun3.makeyourmorning.common.PRIMARY_COLOR
import com.yesnoheun3.makeyourmorning.common.alarmManage.AndroidAlarmScheduler
import java.time.LocalDateTime
import java.util.Calendar

@Composable
fun TimeScreen(onClick: () -> Unit, viewModel: AlarmTimeViewModel) {
    val context = LocalContext.current
    val scheduler = AndroidAlarmScheduler(context)

    val scrollState = rememberLazyListState()

    val instanceItem = viewModel.items

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("취침 시간") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(PRIMARY_COLOR),
                    titleContentColor = Color.Black
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onClick
            ) {
                Icon(Icons.Rounded.Add, "Add alarm")
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Column (
            modifier = Modifier.padding(innerPadding),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(PRIMARY_COLOR))
                    .padding(50.dp),
                contentAlignment = Alignment.Center
            ){
                Text("현재 시간", fontSize = 50.sp)
            }
            LazyColumn(
                state = scrollState
            ) {
                items(
                    count = instanceItem.size,
                    key = { instanceItem[it].id } // 설정해야함
                    ) { index ->
                    SwipeToDeleteContainer(
                        item = instanceItem[index],
                        onDelete = {
                            viewModel.deleteItem(instanceItem[index])
                        }
                    ) {
                        TimeCard(instanceItem[index],
                        onCheckedChanged = { isChecked ->
                            viewModel.updateIsOn(index, isChecked)

                            if (isChecked) {
                                scheduler.schedule(instanceItem[index])
                            } else {
                                scheduler.cancel(instanceItem[index])
                            }
                        })
                    }
                    Divider(
                        color = Color.LightGray,
                        thickness = 1.dp
                    )
                }
            }
        }

    }
}
