package com.koleff.kare_android.ui.compose.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.koleff.kare_android.ui.compose.LoadingWheel
import com.koleff.kare_android.ui.compose.SearchWorkoutList
import com.koleff.kare_android.ui.compose.scaffolds.SearchListScaffold
import com.koleff.kare_android.ui.view_model.WorkoutViewModel

@Composable
fun SearchWorkoutsScreen(
    navController: NavHostController,
    isNavigationInProgress: MutableState<Boolean>,
    exerciseId: Int,
    workoutViewModel: WorkoutViewModel
) {
    SearchListScaffold("Select workout", navController, isNavigationInProgress) { innerPadding ->
        val modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()

        val workoutState by workoutViewModel.state.collectAsState()
        val allWorkouts = workoutState.workoutList

        //Material3 Search bar

        //All workouts
        if (workoutState.isLoading) {
            LoadingWheel()
        } else {
            SearchWorkoutList(
                modifier = modifier,
                workoutList = allWorkouts,
                exerciseId = exerciseId,
                navController = navController
            )
        }
    }
}