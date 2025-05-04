package com.nochunsam.makeyourmorning.pages.setting.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nochunsam.makeyourmorning.common.compose.CustomColumn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingList(
    navController: NavController
) {
    val cardList = listOf<SettingItem>(SettingItem.Tutorial, SettingItem.AllowList)

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("환경 설정") })
        }
    ) { paddingValue ->
        CustomColumn(paddingValues = paddingValue) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                items(count = cardList.size) { index ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate(cardList[index].screenRoute) }
                            .padding(vertical = 15.dp, horizontal = 20.dp)
                    ) {
                        Text(cardList[index].content, fontSize = 15.sp)
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