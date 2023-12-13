package com.koleff.kare_android.ui.compose.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.koleff.kare_android.data.MainScreen
import com.koleff.kare_android.ui.compose.LoadingWheel
import com.koleff.kare_android.ui.compose.banners.AddExerciseToWorkoutBanner
import com.koleff.kare_android.ui.compose.banners.ExerciseBannerV2
import com.koleff.kare_android.ui.compose.scaffolds.MainScreenScaffold
import com.koleff.kare_android.ui.view_model.WorkoutDetailsViewModel

@Composable
fun WorkoutDetailsScreen(
    navController: NavHostController,
    workoutId: Int = -1, //Invalid workout selected...
    isNavigationInProgress: MutableState<Boolean>,
    workoutDetailsViewModel: WorkoutDetailsViewModel
) {
    val workoutDetailsState by workoutDetailsViewModel.state.collectAsState()
    val workoutTitle =
        if (workoutDetailsState.workout.name == "") "Loading..." else workoutDetailsState.workout.name
    val exercises = workoutDetailsState.workout.exercises

    MainScreenScaffold(workoutTitle, navController, isNavigationInProgress) { innerPadding ->
        val modifier = Modifier
            .fillMaxSize()
            .padding(
                PaddingValues(
                    top = 4.dp + innerPadding.calculateTopPadding(),
                    start = 4.dp + innerPadding.calculateStartPadding(LayoutDirection.Rtl),
                    end = 4.dp + innerPadding.calculateEndPadding(LayoutDirection.Rtl),
                    bottom = 4.dp + innerPadding.calculateBottomPadding()
                )
            )

        if (workoutDetailsState.isLoading) {
            LoadingWheel(innerPadding = innerPadding)
        } else {
            LazyColumn(
                modifier = modifier,
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(exercises.size) { currentExerciseId ->
                    val currentExercise = exercises[currentExerciseId]
                    ExerciseBannerV2(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        exercise = currentExercise,
                    ) { selectedExercise ->
                        navController.navigate(
                            MainScreen.ExerciseDetailsConfigurator.createRoute(
                                exerciseId = selectedExercise.exerciseId,
                                workoutId = workoutId,
                                muscleGroupId = selectedExercise.muscleGroup.muscleGroupId
                            )
                        )
                    }
                }

                item {
                    AddExerciseToWorkoutBanner {

                        //Open search exercise screen...
                        openSearchExercisesScreen(
                            navController = navController,
                            workoutId = workoutId
                        )
                    }
                }
            }
        }
    }
}

fun openSearchExercisesScreen(navController: NavHostController, workoutId: Int) {
    navController.navigate(MainScreen.SearchExercisesScreen.createRoute(workoutId))
}

