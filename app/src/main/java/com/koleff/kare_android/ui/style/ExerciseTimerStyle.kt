package com.koleff.kare_android.ui.state

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ExerciseTimerStyle(
    val timerRadius: Dp = 100.dp,
    val totalLines: Int = 30,
    val lineLength: Dp = 35.dp,
    val elapsedLineColor: Color = Color.Yellow
){
    val lineColor: Color
        @Composable
        get() = if(isSystemInDarkTheme()) Color.White else Color.Black
}