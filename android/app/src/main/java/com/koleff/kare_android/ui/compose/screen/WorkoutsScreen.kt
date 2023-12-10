package com.koleff.kare_android.ui.compose.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.koleff.kare_android.ui.compose.scaffolds.MainScreenScaffold
import com.koleff.kare_android.ui.compose.WorkoutSegmentButton

@Composable
fun WorkoutsScreen(navController: NavHostController, isNavigationInProgress: MutableState<Boolean>) {
    MainScreenScaffold("Workouts", navController, isNavigationInProgress) { innerPadding ->
        val modifier = Modifier
            .padding(innerPadding)
            .fillMaxWidth()

        WorkoutSegmentButton(
            modifier = modifier,
            navController = navController,
            selectedOptionIndex = 1,
            isBlocked = isNavigationInProgress
        )
    }
}