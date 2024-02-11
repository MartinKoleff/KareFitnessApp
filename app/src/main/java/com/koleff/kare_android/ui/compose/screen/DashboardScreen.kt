package com.koleff.kare_android.ui.compose.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.koleff.kare_android.ui.compose.LoadingWheel
import com.koleff.kare_android.ui.compose.scaffolds.MainScreenScaffold
import com.koleff.kare_android.ui.compose.MuscleGroupGrid
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.ui.view_model.DashboardViewModel

@Composable
fun DashboardScreen(dashboardViewModel: DashboardViewModel = hiltViewModel()) {
    val onNavigateToDashboard = {
        dashboardViewModel.onNavigationEvent(NavigationEvent.NavigateTo(Destination.Dashboard))
    }
    val onNavigateToWorkouts = {
        dashboardViewModel.onNavigationEvent(NavigationEvent.NavigateTo(Destination.Workouts))
    }
    val onNavigateToSettings = {
        dashboardViewModel.onNavigationEvent(NavigationEvent.NavigateTo(Destination.Settings))
    }
    val onNavigateBack = { dashboardViewModel.onNavigationEvent(NavigationEvent.NavigateBack) }

    MainScreenScaffold(
        "Dashboard",
        onNavigateToDashboard = onNavigateToDashboard,
        onNavigateToWorkouts = onNavigateToWorkouts,
        onNavigateBackAction = onNavigateBack,
        onNavigateToSettings = onNavigateToSettings
    ) { innerPadding ->
        val muscleGroupState by dashboardViewModel.state.collectAsState()

        if (muscleGroupState.isLoading && muscleGroupState.muscleGroupList.isEmpty()) { //Don't show loader if retrieved from cache...
            LoadingWheel(innerPadding = innerPadding)
        } else {
            val modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()

            MuscleGroupGrid(
                modifier = modifier,
                muscleGroupList = muscleGroupState.muscleGroupList
            ) { muscleGroup ->
                dashboardViewModel.navigateToMuscleGroupDetails(muscleGroup)
            }
        }
    }
}