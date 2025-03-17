package com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.koleff.kare_android.ui.compose.components.navigation_components.bottom_navigation_bar.BottomNavigationBar

@Composable
fun NoToolbarScaffold(
    onNavigateToDashboard: () -> Unit,
    onNavigateToWorkouts: () -> Unit,
    modifierPadding: @Composable (paddingValues: PaddingValues) -> Unit
) {
    Scaffold(
        modifier =
        Modifier.fillMaxSize(),
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