package com.koleff.kare_android.ui.compose.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.MainScreenScaffold
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.WorkoutScaffold
import com.koleff.kare_android.ui.view_model.StatisticsViewModel

@Composable
fun StatisticsScreen(
    statisticsViewModel: StatisticsViewModel = hiltViewModel(),
) {
    MainScreenScaffold(
        "Statistics",
        onNavigateToDashboard = { statisticsViewModel.onNavigateToDashboard() },
        onNavigateToWorkouts = { statisticsViewModel.onNavigateToWorkouts() },
        onNavigateBackAction = { statisticsViewModel.onNavigateBack() },
        onNavigateToSettings = { statisticsViewModel.onNavigateToSettings() },
    ) { innerPadding ->

    }
}