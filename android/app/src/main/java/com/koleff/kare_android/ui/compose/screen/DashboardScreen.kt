package com.koleff.kare_android.ui.compose.screen

import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.koleff.kare_android.ui.compose.LoadingWheel
import com.koleff.kare_android.ui.compose.MainScreenScaffold
import com.koleff.kare_android.ui.compose.MuscleGroupGrid
import com.koleff.kare_android.ui.view_model.DashboardViewModel

@Composable
fun DashboardScreen(
    navController: NavHostController,
    isNavigationInProgress: MutableState<Boolean>,
    dashboardViewModel: DashboardViewModel
) {
    MainScreenScaffold("Dashboard", navController, isNavigationInProgress) { innerPadding ->
        val muscleGroupState by dashboardViewModel.state.collectAsState()

        if (muscleGroupState.isLoading && muscleGroupState.muscleGroupList.isEmpty()) { //Don't show loader if retrieved from cache...
            LoadingWheel(innerPadding = innerPadding)
        } else {
            val modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()

            MuscleGroupGrid(
                modifier = modifier,
                navController = navController,
                muscleGroupList = muscleGroupState.muscleGroupList
            )
        }
    }
}