package com.koleff.kare_android.ui.compose.components.navigation_components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.koleff.kare_android.R

@Composable
fun NavigationItem(
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

@Preview
@Composable
fun NavigationItemPreview() {
    NavigationItem(
        modifier = Modifier.size(200.dp),
        icon = painterResource(id = R.drawable.ic_vector_select),
        label = "Select",
        tint = Color.Green
    )
}