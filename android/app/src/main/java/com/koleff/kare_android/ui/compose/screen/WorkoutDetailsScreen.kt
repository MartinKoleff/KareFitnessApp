package com.koleff.kare_android.ui.compose.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.ui.MainScreen
import com.koleff.kare_android.ui.compose.LoadingWheel
import com.koleff.kare_android.ui.compose.banners.AddExerciseToWorkoutBanner
import com.koleff.kare_android.ui.compose.banners.SwipeableExerciseBanner
import com.koleff.kare_android.ui.compose.dialogs.WarningDialog
import com.koleff.kare_android.ui.compose.scaffolds.MainScreenScaffold
import com.koleff.kare_android.ui.view_model.WorkoutDetailsViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WorkoutDetailsScreen(
    navController: NavHostController,
    workoutId: Int = -1, //Invalid workout selected...
    isNavigationInProgress: MutableState<Boolean>,
    workoutDetailsViewModel: WorkoutDetailsViewModel
) {
    val workoutDetailsState by workoutDetailsViewModel.getWorkoutDetailsState.collectAsState()
    val workoutTitle =
        if (workoutDetailsState.workout.name == "") "Loading..." else workoutDetailsState.workout.name

    val onExerciseSelected: (ExerciseDto) -> Unit = { selectedExercise ->

        navController.navigate(
            MainScreen.ExerciseDetailsConfigurator.createRoute(
                exerciseId = selectedExercise.exerciseId,
                workoutId = workoutId,
                muscleGroupId = selectedExercise.muscleGroup.muscleGroupId
            )
        )
    }

    val deleteExerciseState by workoutDetailsViewModel.deleteExerciseState.collectAsState()

    var selectedWorkout by remember {
        mutableStateOf(workoutDetailsState.workout)
    }

    var exercises by remember {
        mutableStateOf(selectedWorkout.exercises)
    }

    var showAddExerciseBanner by remember {
        mutableStateOf(workoutDetailsState.isSuccessful)
    }

    //Update workout on initial load
    LaunchedEffect(key1 = workoutDetailsState) {
        if (workoutDetailsState.isSuccessful) {
            Log.d("WorkoutDetailsScreen", "Initial load completed.")
            selectedWorkout = workoutDetailsState.workout
            exercises = selectedWorkout.exercises
            showAddExerciseBanner = true
        }
    }

    //When exercise is deleted -> update workout exercise list
    LaunchedEffect(key1 = deleteExerciseState) {
        if (deleteExerciseState.isSuccessful) {
            Log.d("WorkoutDetailsScreen", "Exercise successfully deleted.")
            selectedWorkout = deleteExerciseState.workout
            exercises = selectedWorkout.exercises
        }
    }

    //Exercise has been added -> pass to WorkoutsScreen to refresh
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val hasUpdated =
        navBackStackEntry.value?.savedStateHandle?.get<Boolean>("hasUpdated") ?: false
    navController.previousBackStackEntry?.savedStateHandle?.set("hasUpdated", hasUpdated)

    //Dialog visibility
    var showDeleteDialog by remember { mutableStateOf(false) }

    //Dialog callbacks
    var selectedExercise by remember { mutableStateOf<ExerciseDto?>(null) }
    val onExerciseDeleted: (Int, ExerciseDto) -> Unit = { selectedWorkoutId, selectedExercise ->
        workoutDetailsViewModel.deleteExercise(selectedWorkoutId, selectedExercise.exerciseId)

        //Raise a flag to update Workouts screen...
        navController.previousBackStackEntry?.savedStateHandle?.set("hasUpdated", true)
    }

    //Dialogs
    if (showDeleteDialog && selectedExercise != null) {
        WarningDialog(
            title = "Delete Exercise",
            description = "Are you sure you want to delete this exercise? This action cannot be undone.",
            actionButtonTitle = "Delete",
            onClick = { onExerciseDeleted(workoutId, selectedExercise!!) },
            onDismiss = { showDeleteDialog = false }
        )
    }

    //Pull to refresh
    val pullRefreshState = rememberPullRefreshState(
        refreshing = workoutDetailsViewModel.isRefreshing,
        onRefresh = { workoutDetailsViewModel.getWorkoutDetails(workoutId) }
    )

    MainScreenScaffold(workoutTitle, navController, isNavigationInProgress) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        Box(
            Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {

            if (workoutDetailsState.isLoading || deleteExerciseState.isLoading) {
                LoadingWheel(innerPadding = innerPadding)
            } else {
                LazyColumn(
                    modifier = contentModifier,
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Log.d("WorkoutDetailsScreen", "Exercises: $exercises")
                    items(exercises.size) { currentExerciseId ->
                        val currentExercise = exercises[currentExerciseId]

                        SwipeableExerciseBanner(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            exercise = currentExercise,
                            onClick = onExerciseSelected,
                            onDelete = {
                                showDeleteDialog = true
                                selectedExercise = currentExercise
                            }
                        )
                    }

                    item {
                        if (showAddExerciseBanner) { //Show only after data is fetched
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

            PullRefreshIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                refreshing = workoutDetailsViewModel.isRefreshing,
                state = pullRefreshState
            ) //If put as first content -> hides behind the screen...
        }
    }
}

fun openSearchExercisesScreen(navController: NavHostController, workoutId: Int) {
    navController.navigate(MainScreen.SearchExercisesScreen.createRoute(workoutId))
}

