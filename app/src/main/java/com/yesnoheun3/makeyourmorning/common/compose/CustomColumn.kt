package com.yesnoheun3.makeyourmorning.common.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomColumn(color: Color = Color.White,
                 paddingValues: PaddingValues = PaddingValues(0.dp),
                 content: @Composable () -> Unit) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(color)
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        content()
    }
}