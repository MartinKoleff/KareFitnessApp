package com.koleff.kare_android.ui.style

import androidx.compose.ui.graphics.Color

data class OnboardingDataUI(
    val image: Int, val title: String,
    val desc: String,
    val backgroundColor: Color,
    val mainColor: Color = Color.Blue
)