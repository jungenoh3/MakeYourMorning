@file:OptIn(ExperimentalMaterial3Api::class)

package com.yesnoheun3.makeyourmorning.pages.time.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yesnoheun3.makeyourmorning.common.compose.CustomColumn
import com.yesnoheun3.makeyourmorning.pages.time.compose.SwipeToDeleteContainer
import com.yesnoheun3.makeyourmorning.pages.time.compose.TimeCard
import com.yesnoheun3.makeyourmorning.utilities.alarm.AlarmScheduler
import com.yesnoheun3.makeyourmorning.common.data.AlarmTime
import com.yesnoheun3.makeyourmorning.pages.time.data.AlarmTimeViewModel
import com.yesnoheun3.makeyourmorning.ui.theme.Yellow40
import com.yesnoheun3.makeyourmorning.ui.theme.Yellow60
import com.yesnoheun3.makeyourmorning.ui.theme.Yellow80
import kotlinx.coroutines.launch

@Composable
fun TimeScreen(navController: NavController, viewModel: AlarmTimeViewModel) {
    val context = LocalContext.current
    val scheduler = AlarmScheduler(context)

    val scrollState = rememberLazyListState()
    val typeList = listOf<String>("취침 시간", "기상 시간")
    val pageState = rememberPagerState(initialPage = 0, pageCount = { typeList.size })
    val coroutineScope = rememberCoroutineScope()
    val isSleep = pageState.currentPage == 0

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("취침 시간") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Yellow80,
                    titleContentColor = Color.Black
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("addTime?isSleep=${isSleep}") },
                backgroundColor = Yellow60
            ) {
                Icon(Icons.Rounded.Add, "Add alarm")
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        CustomColumn (paddingValues = innerPadding) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Yellow80)
                    .padding(50.dp),
                contentAlignment = Alignment.Center,
            ){
                Text("현재 시간", fontSize = 50.sp)
            }
            Divider(modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Yellow40
                )
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
                            }
                            .background(if (pageState.targetPage == index) {Yellow80} else {Color.White} ),
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
            Divider(modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Yellow40
            )
            HorizontalPager(
                state = pageState
            ) { page ->
                val instanceItem = viewModel.items.filter { it.isSleep == (page == 0) }

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
                                viewModel.deleteItem(instanceItem[index])
                            },
                            onClick = { navController.navigate("addTime?id=${instanceItem[index].id}") }
                        ) {
                            TimeCard(
                                instanceItem[index],
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
