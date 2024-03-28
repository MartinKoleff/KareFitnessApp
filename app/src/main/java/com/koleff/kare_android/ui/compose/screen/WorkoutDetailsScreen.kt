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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.compose.components.LoadingWheel
import com.koleff.kare_android.ui.compose.banners.AddExerciseToWorkoutBanner
import com.koleff.kare_android.ui.compose.banners.SwipeableExerciseBanner
import com.koleff.kare_android.ui.compose.dialogs.WarningDialog
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.MainScreenScaffold
import com.koleff.kare_android.ui.compose.dialogs.ErrorDialog
import com.koleff.kare_android.ui.view_model.WorkoutDetailsViewModel

//TODO: add header to start workout...
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WorkoutDetailsScreen(
    workoutDetailsViewModel: WorkoutDetailsViewModel = hiltViewModel()
) {
    val workoutDetailsState by workoutDetailsViewModel.getWorkoutDetailsState.collectAsState()
    val workoutTitle =
        if (workoutDetailsState.workoutDetails.name == "") "Loading..." else workoutDetailsState.workoutDetails.name

    val onExerciseSelected: (ExerciseDto) -> Unit = { selectedExercise ->

        //TODO: select multiple exercises rework...

        //TODO: submit exercise directly and skip exercise details configurator...

        //TODO: remove exercise details configurator...
        workoutDetailsViewModel.navigateToExerciseDetailsConfigurator(
            exerciseId = selectedExercise.exerciseId,
            workoutId = workoutDetailsState.workoutDetails.workoutId,
            muscleGroupId = selectedExercise.muscleGroup.muscleGroupId
        )
    }

    val deleteExerciseState by workoutDetailsViewModel.deleteExerciseState.collectAsState()

    var selectedWorkout by remember {
        mutableStateOf(workoutDetailsState.workoutDetails)
    }
    var exercises by remember {
        mutableStateOf(selectedWorkout.exercises)
    }
    var selectedExercise by remember { mutableStateOf<ExerciseDto?>(null) }

    var showAddExerciseBanner by remember {
        mutableStateOf(workoutDetailsState.isSuccessful)
    }

    //Update workout on initial load
    LaunchedEffect(workoutDetailsState) {
        if (workoutDetailsState.isSuccessful) {
            Log.d("WorkoutDetailsScreen", "Initial load completed.")
            selectedWorkout = workoutDetailsState.workoutDetails
            exercises = selectedWorkout.exercises
            showAddExerciseBanner = true
        }
    }

    //When exercise is deleted -> update workout exercise list
    LaunchedEffect(deleteExerciseState) {
        if (deleteExerciseState.isSuccessful) {
            Log.d("WorkoutDetailsScreen", "Exercise successfully deleted.")
            selectedWorkout = deleteExerciseState.workoutDetails
            exercises = selectedWorkout.exercises
        }
    }

    //Dialog visibility
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }


    //Dialog callbacks
    val onErrorDialogDismiss = {
        showErrorDialog = false
        workoutDetailsViewModel.clearError() //Enters launched effect to update showErrorDialog...
    }

    //Error handling
    var error by remember { mutableStateOf<KareError?>(null) }
    LaunchedEffect(workoutDetailsState.isError, deleteExerciseState.isError) {

        error = if (workoutDetailsState.isError) {
            workoutDetailsState.error
        } else if (deleteExerciseState.isError) {
            deleteExerciseState.error
        } else {
            null
        }

        showErrorDialog =
            workoutDetailsState.isError || deleteExerciseState.isError

        Log.d("WorkoutDetailsScreen", "Error detected -> $showErrorDialog")
    }

    //Dialogs
    if (showDeleteDialog) {
        WarningDialog(
            title = "Delete Exercise",
            description = "Are you sure you want to delete this exercise? This action cannot be undone.",
            actionButtonTitle = "Delete",
            onClick = {
                selectedExercise?.let {
                    workoutDetailsViewModel.deleteExercise(
                        workoutDetailsState.workoutDetails.workoutId,
                        selectedExercise!!.exerciseId
                    )
                }

            },
            onDismiss = { showDeleteDialog = false }
        )
    }

    if (showErrorDialog) {
        error?.let {
            ErrorDialog(error!!, onErrorDialogDismiss)
        }
    }

    //Pull to refresh
    val pullRefreshState = rememberPullRefreshState(
        refreshing = workoutDetailsViewModel.isRefreshing,
        onRefresh = { workoutDetailsViewModel.getWorkoutDetails(workoutDetailsState.workoutDetails.workoutId) }
    )

    //Navigation Callbacks
    val onNavigateToDashboard = {
        workoutDetailsViewModel.onNavigationEvent(NavigationEvent.NavigateTo(Destination.Dashboard))
    }
    val onNavigateToWorkouts = {
        workoutDetailsViewModel.onNavigationEvent(NavigationEvent.NavigateTo(Destination.Workouts))
    }
    val onNavigateToSettings = {
        workoutDetailsViewModel.onNavigationEvent(NavigationEvent.NavigateTo(Destination.Settings))
    }
    val onNavigateBack = { workoutDetailsViewModel.onNavigationEvent(NavigationEvent.NavigateBack) }

    MainScreenScaffold(
        workoutTitle,
        onNavigateToDashboard = onNavigateToDashboard,
        onNavigateToWorkouts = onNavigateToWorkouts,
        onNavigateBackAction = onNavigateBack,
        onNavigateToSettings = onNavigateToSettings
    ) { innerPadding ->
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
                                selectedExercise = currentExercise
                                showDeleteDialog = true
                            }
                        )
                    }

                    item {
                        if (showAddExerciseBanner) { //Show only after data is fetched
                            AddExerciseToWorkoutBanner {

                                //Open search exercise screen...
                                workoutDetailsViewModel.navigateToSearchExcercises(
                                    workoutId = workoutDetailsState.workoutDetails.workoutId
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


