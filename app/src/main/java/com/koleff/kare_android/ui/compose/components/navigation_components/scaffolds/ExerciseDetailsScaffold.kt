package com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.koleff.kare_android.ui.compose.components.navigation_components.bottom_navigation_bar.ExerciseDetailsBottomNavigationBar
import com.koleff.kare_android.ui.compose.components.navigation_components.toolbar.ExerciseDetailsToolbar

@Composable
fun ExerciseDetailsScaffold(
    screenTitle: String,
    exerciseImageId: Int,
    exerciseId: Int,
    onNavigateAction: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateSubmitExercise: () -> Unit,
    modifierPadding: @Composable (paddingValues: PaddingValues) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ExerciseDetailsToolbar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight / 2.5f),
                exerciseImageId = exerciseImageId,
                onNavigateAction = onNavigateAction,
                onNavigateBackAction = onNavigateBack
            )
        },
        bottomBar = {
            ExerciseDetailsBottomNavigationBar(
                exerciseId = exerciseId,
                onNavigateSubmitExercise = onNavigateSubmitExercise
            )
        }
    ) { innerPadding ->
        modifierPadding(innerPadding)
    }
}