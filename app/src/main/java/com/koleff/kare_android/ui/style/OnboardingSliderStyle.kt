package com.koleff.kare_android.ui.style

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class OnboardingSliderStyle(
    val startBound: Int = 140,
    val endBound: Int = 210,
    val interval: Int = 5,
    val initialValue: Int = 178,
    val totalLines: Int = endBound - startBound,
    val smallLineLength: Dp = 25.dp,
    val bigLineLength: Dp = 35.dp,
    val lineColor: Color = Color.Gray,
)