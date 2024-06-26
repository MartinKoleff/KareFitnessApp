package com.koleff.kare_android.ui.compose.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.koleff.kare_android.ui.compose.components.LoadingWheel
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.MainScreenScaffold
import com.koleff.kare_android.ui.compose.components.MuscleGroupGrid
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.ui.view_model.DashboardViewModel

@Composable
fun DashboardScreen(dashboardViewModel: DashboardViewModel = hiltViewModel()) {
    MainScreenScaffold(
        "Dashboard",
        onNavigateToDashboard = { dashboardViewModel.onNavigateToDashboard() },
        onNavigateToWorkouts = { dashboardViewModel.onNavigateToWorkouts() },
        onNavigateBackAction = { dashboardViewModel.onNavigateBack() },
        onNavigateToSettings = { dashboardViewModel.onNavigateToSettings() }
    ) { innerPadding ->
        val muscleGroupState by dashboardViewModel.state.collectAsState()

        if (muscleGroupState.isLoading && muscleGroupState.muscleGroupList.isEmpty()) { //Don't show loader if retrieved from cache...
            LoadingWheel(innerPadding = innerPadding)
        } else {
            val modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)

            MuscleGroupGrid(
                modifier = modifier,
                muscleGroupList = muscleGroupState.muscleGroupList
            ) { muscleGroup ->
                dashboardViewModel.navigateToMuscleGroupDetails(muscleGroup)
            }
        }
    }
}