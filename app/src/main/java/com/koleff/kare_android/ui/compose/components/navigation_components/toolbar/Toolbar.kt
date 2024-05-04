package com.koleff.kare_android.ui.compose.components.navigation_components.toolbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.koleff.kare_android.ui.compose.components.navigation_components.NavigationItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar(
    modifier: Modifier = Modifier,
    textAlpha: Float = 1f,
    title: String = "",
    hasTitle: Boolean = true,
    onNavigateBackAction: () -> Unit,
    onNavigateToAction: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    CenterAlignedTopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            if (hasTitle) {
                Box(
                    modifier = Modifier.fillMaxSize().alpha(textAlpha),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
            }
        },
        navigationIcon = {
            NavigationItem(
                modifier = Modifier.fillMaxHeight(),
                icon = Icons.AutoMirrored.Filled.ArrowBackIos,
                label = "Go back",
                onNavigateAction = onNavigateBackAction
            )
        },
        actions = {
            NavigationItem(
                modifier = Modifier.fillMaxHeight(),
                icon = Icons.Filled.Settings,
                label = "Settings",
                onNavigateAction = onNavigateToAction
            )
        },
        scrollBehavior = scrollBehavior
    )
}

@Preview
@Composable
fun ToolbarPreview() {
    //Adding modifier moves all components up. Without modifier all components are centered.

    Toolbar(
        modifier = Modifier.height(100.dp),
        textAlpha = 1f, //Hide text until passed the 2 line threshold -> glitch with 2 lined text on 1 line.
        title = "Arnold explosive chest workout",
        onNavigateBackAction = {},
        onNavigateToAction = {}
    )
}