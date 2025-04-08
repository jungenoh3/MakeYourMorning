package com.yesnoheun3.makeyourmorning

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigation
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigationItem
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yesnoheun3.makeyourmorning.pages.time.TimeScreen
import com.yesnoheun3.makeyourmorning.pages.TaskRecord
import com.yesnoheun3.makeyourmorning.pages.User
import com.yesnoheun3.makeyourmorning.pages.time.AddTimeScreen
import com.yesnoheun3.makeyourmorning.pages.time.SleepTimeModel

sealed class BottomNavItem(
    val title: String,
    val screenRoute: String,
    val icon: ImageVector
){
    object TimeSetting : BottomNavItem("시간 설정", "time", Icons.Rounded.Notifications)
    object TaskRecord : BottomNavItem("한 일들", "taskRecord", Icons.Rounded.DateRange)
    object User : BottomNavItem("설정", "user", Icons.Rounded.Person)
}

@Composable
fun NavigationGraph(navController: NavHostController){
    val viewModel: SleepTimeModel = viewModel()

    NavHost(navController = navController, startDestination = BottomNavItem.TimeSetting.screenRoute) {
        composable(route =  BottomNavItem.TimeSetting.screenRoute) {
            TimeScreen(onClick = { navController.navigate("addTime") }, viewModel = viewModel)
        }
        composable(route = BottomNavItem.TaskRecord.screenRoute) {
            TaskRecord()
        }
        composable(route = BottomNavItem.User.screenRoute) {
            User()
        }
        composable(route = "addTime") {
            AddTimeScreen(popBack = { navController.popBackStack() }, viewModel = viewModel)
        }
    }
}


@SuppressLint("LaunchDuringComposition")
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val items = listOf(
        BottomNavItem.TimeSetting,
        BottomNavItem.TaskRecord,
        BottomNavItem.User
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigation(
                modifier = Modifier
                    .height(70.dp),
                backgroundColor = Color.White,
                contentColor = Color.DarkGray
            ) {
                items.forEach { item ->
                    BottomNavigationItem(
                        modifier = Modifier
                            .align(alignment = Alignment.CenterVertically),
                        label = @Composable {
                            Text(
                                text = item.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        },
                        selected = currentRoute == item.screenRoute,
                        onClick = {
                            navController.navigate(item.screenRoute)  {
                                navController.graph.startDestinationRoute?.let {
                                    popUpTo(it) {
                                        saveState = true
                                    }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                item.icon,
                                "Navigation button",
                                modifier = Modifier.padding(6.dp))
                        }
                    )
                }
            }
        }
    ) {innerPadding ->
        CustomNavGraph(innerPadding, navController)
    }
}

@Composable
fun CustomNavGraph(innerPadding: PaddingValues, navController: NavHostController) {
    Box(modifier = Modifier.padding(innerPadding)){
        NavigationGraph(navController = navController)
    }

}