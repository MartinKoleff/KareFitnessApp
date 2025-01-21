package com.koleff.kare_android.data.model.dto

import androidx.compose.ui.graphics.Color

data class OnBoardingData(
    val image: Int, val title: String,
    val desc: String,
    val backgroundColor: Color,
    val mainColor: Color = Color.Blue
)