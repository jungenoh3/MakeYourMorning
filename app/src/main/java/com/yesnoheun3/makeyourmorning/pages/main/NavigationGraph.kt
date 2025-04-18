package com.yesnoheun3.makeyourmorning.pages.main

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yesnoheun3.makeyourmorning.pages.sleep.Sleeping
import com.yesnoheun3.makeyourmorning.pages.time.AddTimeScreen
import com.yesnoheun3.makeyourmorning.pages.time.data.AlarmTimeViewModel

@Composable
fun NavigationGraph(startDestination: String){
    val viewModel: AlarmTimeViewModel = viewModel()
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        composable(route =  "main") {
            MainScreen(rootNavController = navController, viewModel = viewModel)
        }
        composable(route = "sleeping") {
            Sleeping()
        }
        composable(route = "addTime?id={id}&isSleep={isSleep}",
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            },
                navArgument("isSleep") {
                    type = NavType.BoolType
                    nullable = false
                    defaultValue = true
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            val isSleep = backStackEntry.arguments?.getBoolean("isSleep")
            AddTimeScreen(popBack = { navController.popBackStack() }, viewModel = viewModel, id = id, isSleep = isSleep!!)
        }
    }
}