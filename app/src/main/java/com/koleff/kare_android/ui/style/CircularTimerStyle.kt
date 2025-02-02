package com.koleff.kare_android.ui.style

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class CircularTimerStyle(
    val size: Dp = 120.dp,
    val cellSize: Dp = size / 3,
    val cellTextSize: Dp = cellSize / 2
)