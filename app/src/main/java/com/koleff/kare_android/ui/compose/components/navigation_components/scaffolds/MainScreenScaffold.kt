package com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.koleff.kare_android.ui.compose.components.navigation_components.bottom_navigation_bar.BottomNavigationBar
import com.koleff.kare_android.ui.compose.components.navigation_components.toolbar.Toolbar

@Composable
fun MainScreenScaffold(
    screenTitle: String,
    onNavigateToSettings: () -> Unit,
    onNavigateBackAction: () -> Unit,
    onNavigateToDashboard: () -> Unit,
    onNavigateToWorkouts: () -> Unit,
    showToolbar: Boolean = true,
    toolbarHeight: Dp? = null,
    modifierPadding: @Composable (paddingValues: PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (showToolbar) {
                Toolbar(
                    modifier = if (toolbarHeight == null) Modifier
                                     else Modifier.height(toolbarHeight),
                    title = screenTitle,
                    onNavigateToAction = onNavigateToSettings,
                    onNavigateBackAction = onNavigateBackAction
                )
            }
        },
        bottomBar = {
            BottomNavigationBar(
                onNavigateToDashboard = onNavigateToDashboard,
                onNavigateToWorkouts = onNavigateToWorkouts
            )
        }
    ) { innerPadding ->
        modifierPadding(innerPadding)
    }
}