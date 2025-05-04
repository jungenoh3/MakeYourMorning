package com.nochunsam.makeyourmorning.pages.main.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nochunsam.makeyourmorning.pages.day.screen.DayManagerScreen
import com.nochunsam.makeyourmorning.pages.main.data.BottomNavItem
import com.nochunsam.makeyourmorning.pages.time.data.AlarmTimeViewModel
import com.nochunsam.makeyourmorning.pages.time.screen.TimeScreen
import com.nochunsam.makeyourmorning.pages.setting.screen.Setting

@Composable
fun InnerNavGraph(innerPadding: PaddingValues, navController: NavHostController,
                  rootNavController: NavHostController, viewModel: AlarmTimeViewModel) {
    Box(modifier = Modifier.padding(innerPadding)){
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.TimeSetting.screenRoute,
        ) {
            composable(BottomNavItem.TimeSetting.screenRoute) {
                TimeScreen(navController = rootNavController, viewModel = viewModel)
            }
            composable(BottomNavItem.DayManage.screenRoute) {
                DayManagerScreen()
            }
            composable(BottomNavItem.Setting.screenRoute) {
                Setting()
            }
        }
    }
}