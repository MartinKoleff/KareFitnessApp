package com.koleff.kare_android.ui.state

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ExerciseTimerStyle(
    val timerRadius: Dp = 150.dp,
    val totalLines: Int = 30,
    val lineLength: Dp = 35.dp,
    val lineColor: Color = Color.Gray,
    val elapsedLineColor: Color = Color.Yellow
)