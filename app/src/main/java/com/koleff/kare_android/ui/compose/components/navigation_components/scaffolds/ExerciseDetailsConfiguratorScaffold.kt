package com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds

import com.koleff.kare_android.ui.compose.components.navigation_components.toolbar.ExerciseDetailsConfiguratorToolbar
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.koleff.kare_android.ui.compose.shapes.RoundedToolbarShape

@Composable
fun ExerciseDetailsConfiguratorScaffold(
    screenTitle: String,
    exerciseImageId: Int,
    onSubmitExercise: () -> Unit,
    onNavigateBackAction: () -> Unit,
    modifierPadding: @Composable (paddingValues: PaddingValues) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ExerciseDetailsConfiguratorToolbar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight / 2.5f),
                exerciseImageId = exerciseImageId,
                onSubmitExercise = onSubmitExercise,
                onNavigateBackAction = onNavigateBackAction
            )
        },
    ) { innerPadding ->
        modifierPadding(innerPadding)
    }
}