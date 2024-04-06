package com.koleff.kare_android.ui.state

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

data class CircularTimerStyle(
    val size: Dp = 160.dp,
    val cellSize: Dp = size / 3,
    val cellTextSize: Dp = cellSize / 2
)