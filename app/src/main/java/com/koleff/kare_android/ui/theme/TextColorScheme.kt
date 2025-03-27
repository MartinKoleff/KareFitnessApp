package com.koleff.kare_android.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class TextColorScheme(
    val title: Color,
    val subtitle: Color,
    val label: Color
)

val LocalExtendedColors = staticCompositionLocalOf {
    TextColorScheme(
        title = Color.Unspecified,
        subtitle = Color.Unspecified,
        label = Color.Unspecified
    )
}