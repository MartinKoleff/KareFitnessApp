package com.koleff.kare_android.ui.style

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ExerciseTimerStyle(
    val timerRadius: Dp = 100.dp,
    val totalLines: Int = 30,
    val lineLength: Dp = 35.dp,
    val lineColor: Color,
    val elapsedLineColor: Color = Color.Yellow
)