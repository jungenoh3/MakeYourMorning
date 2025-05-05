@file:OptIn(ExperimentalMaterial3Api::class)

package com.nochunsam.makeyourmorning.pages.time.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.FloatingActionButton
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.nochunsam.makeyourmorning.common.compose.CustomColumn
import com.nochunsam.makeyourmorning.pages.time.compose.SwipeToDeleteContainer
import com.nochunsam.makeyourmorning.pages.time.compose.TimeCard
import com.nochunsam.makeyourmorning.utilities.alarm.AlarmScheduler
import com.nochunsam.makeyourmorning.common.data.AlarmTime
import com.nochunsam.makeyourmorning.pages.time.data.AlarmTimeViewModel

@Composable
fun TimeScreen(
    navController: NavController,
    viewModel: AlarmTimeViewModel
) {
    val context = LocalContext.current
    val scheduler = AlarmScheduler(context)

    val scrollState = rememberLazyListState()
    val typeList = listOf<String>("취침 시간") // , "기상 시간")
    val pageState = rememberPagerState(initialPage = 0, pageCount = { typeList.size })
    val isSleep = pageState.currentPage == 0

    val item = viewModel.items.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("취침 시간 설정") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("addTime?isSleep=${isSleep}") },
                backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Icon(Icons.Rounded.Add, "Add alarm")
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        CustomColumn(paddingValues = innerPadding) {
            HorizontalPager(
                state = pageState
            ) { page ->
                val instanceItem = item.value.filter { it.isSleep == (page == 0) }

                LazyColumn(
                    state = scrollState,
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top
                ) {
                    items(
                        count = instanceItem.size,
                        key = { instanceItem[it].id } // 설정해야함
                    ) { index ->
                        SwipeToDeleteContainer<AlarmTime>(
                            item = instanceItem[index],
                            onDelete = {
                                scheduler.cancel(instanceItem[index].id)
                                viewModel.deleteItem(instanceItem[index])
                            },
                        ) {
                            TimeCard(
                                instanceItem[index],
                                onClick = {
                                    System.out.println("Card selected: ${index}")
                                    navController.navigate("addTime?id=${instanceItem[index].id}")
                                          },
                                onCheckedChanged = { isChecked ->
                                    viewModel.updateIsOn(instanceItem[index].id, isChecked)
                                    if (isChecked) {
                                        scheduler.scheduleAlarm(instanceItem[index])
                                    } else {
                                        scheduler.cancel(instanceItem[index].id)
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }

    }
}
