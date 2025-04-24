package com.yesnoheun3.makeyourmorning.pages.main.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yesnoheun3.makeyourmorning.pages.day.screen.DayManagerScreen
import com.yesnoheun3.makeyourmorning.pages.main.data.BottomNavItem
import com.yesnoheun3.makeyourmorning.pages.sleep.screen.SleepManagerScreen
import com.yesnoheun3.makeyourmorning.pages.time.data.AlarmTimeViewModel
import com.yesnoheun3.makeyourmorning.pages.time.screen.TimeScreen
import com.yesnoheun3.makeyourmorning.pages.user.User

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
            composable(BottomNavItem.GoToSleep.screenRoute) {
                DayManagerScreen()
            }
            composable(BottomNavItem.User.screenRoute) {
                User()
            }
        }
    }
}