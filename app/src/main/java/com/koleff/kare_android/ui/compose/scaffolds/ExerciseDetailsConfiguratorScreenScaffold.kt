package com.koleff.kare_android.ui.compose.scaffolds

import ExerciseDetailsConfiguratorToolbar
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
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.koleff.kare_android.ui.compose.navigation.shapes.RoundedToolbarShape

@Composable
fun ExerciseDetailsConfiguratorScreenScaffold(
    screenTitle: String,
    navController: NavHostController,
    isNavigationInProgress: MutableState<Boolean>,
    exerciseImageId: Int,
    onSubmitExercise: () -> Unit,
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
                    .height(screenHeight / 2.5f)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer
                    )
                    .border(
                        border = BorderStroke(2.dp, color = Color.White),
                        shape = RoundedToolbarShape(hasTopOutline = false)
                    ),
                isNavigationInProgress = isNavigationInProgress,
                navController = navController,
                exerciseImageId = exerciseImageId,
                onSubmitExercise = onSubmitExercise
            )
        },
    ) { innerPadding ->
        modifierPadding(innerPadding)
    }
}