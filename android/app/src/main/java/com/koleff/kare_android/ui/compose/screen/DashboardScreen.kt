package com.koleff.kare_android.ui.compose.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.koleff.kare_android.ui.compose.MainScreenScaffold
import com.koleff.kare_android.ui.compose.MuscleGroupGrid
import com.koleff.kare_android.ui.view_model.DashboardViewModel

@Composable
fun DashboardScreen(
    navController: NavHostController,
    isNavigationInProgress: MutableState<Boolean>,
    dashboardViewModel: DashboardViewModel = hiltViewModel()
) {
    MainScreenScaffold("Dashboard", navController, isNavigationInProgress) { innerPadding ->
        val muscleGroupList = dashboardViewModel.state.collectAsState()

        val modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()

        MuscleGroupGrid(
            modifier = modifier,
            navController = navController,
            muscleGroupList = muscleGroupList.value.muscleGroupList
        )
    }
}