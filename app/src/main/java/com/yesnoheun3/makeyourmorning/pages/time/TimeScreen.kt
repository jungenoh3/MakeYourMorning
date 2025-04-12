@file:OptIn(ExperimentalMaterial3Api::class)

package com.yesnoheun3.makeyourmorning.pages.time

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yesnoheun3.makeyourmorning.common.PRIMARY_COLOR
import com.yesnoheun3.makeyourmorning.common.alarmManage.AndroidAlarmScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun TimeScreen(navController: NavController, viewModel: AlarmTimeViewModel) {
    val context = LocalContext.current
    val scheduler = AndroidAlarmScheduler(context)

    val scrollState = rememberLazyListState()
    val typeList = listOf<String>("취침 시간", "기상 시간")
    val pageState = rememberPagerState(initialPage = 0, pageCount = { typeList.size })
    val coroutineScope = rememberCoroutineScope()
    val isSleep = remember { mutableStateOf(true) } // true면 sleep

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
                onClick = { navController.navigate("addTime?isSleep=${isSleep.value}") }
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
                contentAlignment = Alignment.Center,
            ){
                Text("현재 시간", fontSize = 50.sp)
            }
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                typeList.forEachIndexed { index, title ->
                    Box(
                        modifier = Modifier.
                        fillMaxWidth()
                            .weight(0.5f)
                            .clickable{
                                    coroutineScope.launch {
                                        pageState.animateScrollToPage(index)
                                    }
                                    if (index == 0) {
                                        isSleep.value = true
                                    } else {
                                        isSleep.value = false
                                    }
                            }
                            .background(if (pageState.targetPage == index) {Color(PRIMARY_COLOR)} else {Color.White} ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            modifier = Modifier
                                .padding(16.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

            }
            HorizontalPager(
                state = pageState
            ) {
                LazyColumn(
                    state = scrollState,
                ) {
                    val instanceItem = viewModel.items.filter { it.isSleep == isSleep.value }
                    items(
                        count = instanceItem.size,
                        key = { instanceItem[it].id } // 설정해야함
                    ) { index ->
                        SwipeToDeleteContainer<AlarmTime>(
                            item = instanceItem[index],
                            onDelete = {
                                viewModel.deleteItem(instanceItem[index])
                            }
                        ) {
                            TimeCard(
                                instanceItem[index],
                                onCheckedChanged = { isChecked ->
                                    viewModel.updateIsOn(instanceItem[index].id, isChecked)
                                    if (isChecked) {
                                        scheduler.schedule(instanceItem[index])
                                    } else {
                                        scheduler.cancel(instanceItem[index].id)
                                    }
                                },
                                onClick = { navController.navigate("addTime?id=${instanceItem[index].id}") }
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

    }
}
