package com.nochunsam.makeyourmorning.pages.main.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigation
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigationItem
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nochunsam.makeyourmorning.pages.main.compose.InnerNavGraph
import com.nochunsam.makeyourmorning.pages.main.data.BottomNavItem
import com.nochunsam.makeyourmorning.pages.time.data.AlarmTimeViewModel


@SuppressLint("LaunchDuringComposition")
@Composable
fun MainScreen(rootNavController: NavHostController, viewModel: AlarmTimeViewModel) {
    val navController = rememberNavController()
    val items = listOf(
        BottomNavItem.TimeSetting,
        BottomNavItem.DayManage,
        BottomNavItem.Setting
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigation(
                modifier = Modifier
                    .height(70.dp),
                backgroundColor = MaterialTheme.colorScheme.surfaceBright,
            ) {
                items.forEach { item ->
                    BottomNavigationItem(
                        modifier = Modifier
                            .align(alignment = Alignment.CenterVertically),
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
                                imageVector = ImageVector.vectorResource(item.icon),
                                contentDescription = "Navigation button to ${item.screenRoute}",
                                modifier = Modifier.padding(6.dp)
                            )
                        },
                        label = {
                            Text(
                                text = item.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        },
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
    ) {innerPadding ->
        InnerNavGraph(innerPadding, navController, rootNavController, viewModel)
    }
}

