@file:OptIn(ExperimentalMaterial3Api::class)

package com.yesnoheun3.makeyourmorning

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.yesnoheun3.pages.taskrecord.TaskRecord
import com.yesnoheun3.pages.TimeSetting
import com.yesnoheun3.pages.User

@Composable
fun MainScreen(modifier: Modifier = Modifier) {

    val navItemList = listOf(
        NavItem("TimeSetting", Icons.Rounded.Notifications),
        NavItem("TaskRecord", Icons.Rounded.Create),
        NavItem("User", Icons.Rounded.Person)
    )

    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                navItemList.forEachIndexed{ index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                                  selectedIndex = index
                        },
                        icon = { Icon(imageVector = navItem.icon, contentDescription = "Icon") },
                        label ={
                            Text(text = navItem.label)
                        }
                    )
                }
            }
        }
    ) {innerPadding ->
        ContentScreen(modifier = Modifier.padding(innerPadding), selectedIndex)
    }
}

@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex : Int) {
    System.out.println("selectedIndex: $selectedIndex")

    when(selectedIndex){
        0-> TimeSetting()
        1-> TaskRecord()
        2-> User()
    }
}