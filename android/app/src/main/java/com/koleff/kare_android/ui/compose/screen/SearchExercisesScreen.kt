package com.koleff.kare_android.ui.compose.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.event.OnWorkoutDetailsEvent
import com.koleff.kare_android.ui.compose.LoadingWheel
import com.koleff.kare_android.ui.compose.SearchExercisesList
import com.koleff.kare_android.ui.compose.scaffolds.SearchListScaffold
import com.koleff.kare_android.ui.view_model.ExerciseListViewModel
import com.koleff.kare_android.ui.view_model.WorkoutDetailsViewModel

@Composable
fun SearchExercisesScreen(
    navController: NavHostController,
    isNavigationInProgress: MutableState<Boolean>,
    workoutId: Int,
    exercisesListViewModel: ExerciseListViewModel
) {
    SearchListScaffold("Select exercise", navController, isNavigationInProgress) { innerPadding ->
        val modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()

        val exercisesState by exercisesListViewModel.state.collectAsState()
        val allExercises = exercisesState.exerciseList

        //Material3 Search bar

        //All exercises
        if (exercisesState.isLoading) {
            LoadingWheel()
        } else {
            SearchExercisesList(
                modifier = modifier,
                exerciseList = allExercises,
                workoutId = workoutId,
                navController = navController
            )
        }
    }
}