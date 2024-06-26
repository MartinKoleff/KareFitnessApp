package com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.koleff.kare_android.ui.compose.components.navigation_components.bottom_navigation_bar.BottomNavigationBar
import com.koleff.kare_android.ui.compose.components.navigation_components.toolbar.DoWorkoutToolbar
import com.koleff.kare_android.ui.compose.components.navigation_components.toolbar.Toolbar

@Composable
fun DoWorkoutScaffold(
    modifier: Modifier,
    screenTitle: String,
    onExitWorkoutAction: () -> Unit,
    onNextExerciseAction: () -> Unit,
    modifierPadding: @Composable (paddingValues: PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            DoWorkoutToolbar(
                title = screenTitle,
                onExitWorkoutAction = onExitWorkoutAction,
                onNextExerciseAction = onNextExerciseAction
            )
        }
    ) { innerPadding ->
        modifierPadding(innerPadding)
    }
}