package com.koleff.kare_android.ui.state

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class AnimatedToolbarState(
    val textAlpha: Float = 1f,
    val showToolbar: Boolean = true,
    val defaultToolbarHeight: Dp = 65.dp,
    val toolbarHeight: Dp = defaultToolbarHeight
) {
}