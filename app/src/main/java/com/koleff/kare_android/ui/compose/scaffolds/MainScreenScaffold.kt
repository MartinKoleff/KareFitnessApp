package com.koleff.kare_android.ui.compose.scaffolds

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.koleff.kare_android.ui.compose.components.navigation_components.BottomNavigationBar
import com.koleff.kare_android.ui.compose.components.navigation_components.Toolbar

@Composable
fun MainScreenScaffold(
    screenTitle: String,
    onNavigateToSettings: () -> Unit,
    onNavigateBackAction: () -> Unit,
    onNavigateToDashboard: () -> Unit,
    onNavigateToWorkouts: () -> Unit,
    modifierPadding: @Composable (paddingValues: PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Toolbar(
                title = screenTitle,
                onNavigateToAction = onNavigateToSettings,
                onNavigateBackAction = onNavigateBackAction
            )
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