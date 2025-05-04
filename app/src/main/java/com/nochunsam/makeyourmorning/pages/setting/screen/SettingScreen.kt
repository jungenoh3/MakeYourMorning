package com.nochunsam.makeyourmorning.pages.setting.screen

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

sealed class SettingItem(
    val content: String,
    val screenRoute: String,
) {
    object Setting : SettingItem("설정", "settingList")
    object Tutorial : SettingItem("사용 설명", "tutorial")
    object AllowList : SettingItem("앱 허용 리스트", "allowList")
}

@Composable
fun Setting() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = SettingItem.Setting.screenRoute) {
        composable(SettingItem.Setting.screenRoute) { SettingList(navController) }
        composable(SettingItem.Tutorial.screenRoute) { TutorialScreen() }
        composable(SettingItem.AllowList.screenRoute) { AllowListScreen() }
    }
}