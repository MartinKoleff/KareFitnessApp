package com.koleff.kare_android.ui.compose.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.koleff.kare_android.ui.compose.LoadingWheel
import com.koleff.kare_android.ui.compose.banners.WorkoutBannerList
import com.koleff.kare_android.ui.compose.WorkoutSegmentButton
import com.koleff.kare_android.ui.compose.scaffolds.MainScreenScaffold
import com.koleff.kare_android.ui.view_model.WorkoutViewModel

@Composable
fun WorkoutsScreen(
    navController: NavHostController,
    isNavigationInProgress: MutableState<Boolean>,
    workoutListViewModel: WorkoutViewModel
) {
    MainScreenScaffold("Workouts", navController, isNavigationInProgress) { innerPadding ->
        val modifier = Modifier
            .padding(innerPadding)
            .fillMaxWidth()

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            //Filter buttons
            WorkoutSegmentButton(
                modifier = modifier,
                navController = navController,
                selectedOptionIndex = 1,
                isBlocked = isNavigationInProgress,
                workoutListViewModel = workoutListViewModel
            )

            val workoutState by workoutListViewModel.state.collectAsState()

            if (workoutState.isLoading && workoutState.workoutList.isEmpty()) { //Don't show loader if retrieved from cache...
                LoadingWheel(innerPadding = innerPadding)
            } else {
                WorkoutBannerList(
                    innerPadding = innerPadding,
                    navController = navController,
                    workoutList = workoutState.workoutList
                )
            }
        }
    }
}