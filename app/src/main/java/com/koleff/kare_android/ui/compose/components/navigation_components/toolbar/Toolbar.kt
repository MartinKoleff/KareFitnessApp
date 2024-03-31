package com.koleff.kare_android.ui.compose.components.navigation_components.toolbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.koleff.kare_android.ui.compose.components.navigation_components.NavigationItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar(
    modifier: Modifier = Modifier,
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
                Text(
                    text = title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        },
        navigationIcon = {
            NavigationItem(
                icon = Icons.Filled.ArrowBack,
                label = "Go back",
                onNavigateAction = onNavigateBackAction
            )
        },
        actions = {
            NavigationItem(
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
    Toolbar(
        onNavigateBackAction = {},
        onNavigateToAction = {}
    )
}