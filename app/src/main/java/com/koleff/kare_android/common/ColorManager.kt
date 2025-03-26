package com.koleff.kare_android.common

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat

object ColorManager {
    @Composable
    fun colorFromResource(context: Context, resId: Int): Color {
        return Color(ContextCompat.getColor(context, resId))
    }
}