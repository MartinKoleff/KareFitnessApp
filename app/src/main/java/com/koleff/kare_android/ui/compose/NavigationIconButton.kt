package com.koleff.kare_android.ui.compose

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun NavigationIconButton(
    modifier: Modifier = Modifier,
    icon: Any, //Can be Painter or ImageVector
    label: String,
    tint: Color? = null, //Color.Black
    onNavigateAction: () -> Unit = {}
) {
    IconButton(
        modifier = modifier,
        onClick = {
            onNavigateAction()
        }
    ) {
        when (icon) {
            is ImageVector -> {
                tint?.let {
                    Icon(imageVector = icon, contentDescription = label, tint = tint)
                } ?: run {
                    Icon(imageVector = icon, contentDescription = label)
                }
            }

            is Painter -> {
                tint?.let {
                    Icon(icon, contentDescription = label, tint = tint)
                } ?: run {
                    Icon(icon, contentDescription = label)
                }
            }

            else -> return@IconButton
        }
    }
}