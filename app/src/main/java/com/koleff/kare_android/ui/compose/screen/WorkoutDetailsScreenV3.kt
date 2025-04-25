package com.koleff.kare_android.ui.compose.screen

import WorkoutConfigurationHeader
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.WorkoutConfigurationDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.compose.components.ExerciseBannerV2
import com.koleff.kare_android.ui.compose.components.LoadingWheel
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.MainScreenScaffold
import com.koleff.kare_android.ui.compose.dialogs.DeleteExerciseDialog
import com.koleff.kare_android.ui.compose.dialogs.DeleteMultipleExercisesDialog
import com.koleff.kare_android.ui.compose.dialogs.EditWorkoutDialog
import com.koleff.kare_android.ui.compose.dialogs.ErrorDialog
import com.koleff.kare_android.ui.compose.dialogs.FavoriteWorkoutDialog
import com.koleff.kare_android.ui.compose.dialogs.WarningDialog
import com.koleff.kare_android.ui.compose.dialogs.WorkoutConfigurationDialog
import com.koleff.kare_android.ui.event.OnMultipleExercisesUpdateEvent
import com.koleff.kare_android.ui.state.BaseState
import com.koleff.kare_android.ui.view_model.WorkoutDetailsViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WorkoutDetailsScreenV3(
    workoutDetailsViewModel: WorkoutDetailsViewModel = hiltViewModel()
) {
    val workoutDetailsState by workoutDetailsViewModel.getWorkoutDetailsState.collectAsState()
    val updateWorkoutDetailsState by workoutDetailsViewModel.updateWorkoutDetailsState.collectAsState()
    val deleteWorkoutDetailsState by workoutDetailsViewModel.deleteWorkoutState.collectAsState()
    val deleteExerciseState by workoutDetailsViewModel.deleteExerciseState.collectAsState()
    val startWorkoutState by workoutDetailsViewModel.startWorkoutState.collectAsState()
    val createWorkoutState by workoutDetailsViewModel.createWorkoutState.collectAsState()
    val favoriteWorkoutState by workoutDetailsViewModel.favoriteWorkoutState.collectAsState()
    val unfavoriteWorkoutState by workoutDetailsViewModel.unfavoriteWorkoutState.collectAsState()

    val workoutTitle =
        if (workoutDetailsState.workoutDetails.name == "" || updateWorkoutDetailsState.isLoading) "Loading..."
        else workoutDetailsState.workoutDetails.name

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
    var showDeleteExerciseDialog by remember { mutableStateOf(false) }
    var showDeleteMultipleExercisesDialog by remember { mutableStateOf(false) }
    var showEditWorkoutNameDialog by remember { mutableStateOf(false) }
    var showFavoriteDialog by remember { mutableStateOf(false) }
    var showUnfavoriteDialog by remember { mutableStateOf(false) }
    var showDeleteWorkoutDialog by remember { mutableStateOf(false) }
    var showWorkoutConfigureDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var showLoadingDialog by remember { mutableStateOf(false) }

    //Dialog callbacks
    val onDeleteWorkout: () -> Unit = {
        workoutDetailsViewModel.deleteWorkout()

        showDeleteWorkoutDialog = false
    }

    val onFavoriteWorkout: () -> Unit = {
        workoutDetailsViewModel.favoriteWorkout(selectedWorkout.workoutId)

        showFavoriteDialog = false
    }

    val onUnfavoriteWorkout: () -> Unit = {
        workoutDetailsViewModel.unfavoriteWorkout(selectedWorkout.workoutId)

        showUnfavoriteDialog = false
    }


    val onEditWorkoutName: (String) -> Unit = { newName ->
        val updatedWorkout = selectedWorkout.copy(name = newName)

        //Update workout
        workoutDetailsViewModel.updateWorkout(updatedWorkout)

        showEditWorkoutNameDialog = false
    }

    val onUpdateWorkoutConfiguration: (WorkoutConfigurationDto) -> Unit = { configuration ->
        workoutDetailsViewModel.updateWorkoutConfiguration(
            configuration
        )

        showWorkoutConfigureDialog = false
    }

    var isDeleteMode by remember {
        mutableStateOf(false)
    }
    val onDeleteModeEnabled = {
        isDeleteMode = !isDeleteMode
    }

    val selectedExercises = remember {
        mutableStateListOf<ExerciseDto>()
    }

    val onDeleteMultipleExercises: () -> Unit = {  //(List<ExerciseDto>) -> Unit
        workoutDetailsViewModel.onMultipleExercisesUpdateEvent(
            OnMultipleExercisesUpdateEvent.OnMultipleExercisesDelete(selectedExercises)
        )

        selectedExercises.clear()
        isDeleteMode = false
        showDeleteMultipleExercisesDialog = false
    }

    val onDeleteExercise = {
        selectedExercise?.let {
            workoutDetailsViewModel.deleteExercise(
                workoutDetailsState.workoutDetails.workoutId,
                selectedExercise!!.exerciseId
            )
        }

        showDeleteExerciseDialog = false
    }

    val onExerciseSelected: (ExerciseDto) -> Unit = { selectedExercise ->
        if (isDeleteMode) {
            val isNewExercise = !selectedExercises.map { it.exerciseId }
                .contains(selectedExercise.exerciseId)

            if (isNewExercise) {
                selectedExercises.add(
                    selectedExercise.copy(workoutId = workoutDetailsState.workoutDetails.workoutId)
                )
            } else {
                selectedExercises.removeAll { it.exerciseId == selectedExercise.exerciseId }

                isDeleteMode = selectedExercises.isNotEmpty()
            }
        } else {
            workoutDetailsViewModel.navigateToExerciseDetailsConfigurator(selectedExercise)
        }
    }

    //Error handling
    var error by remember { mutableStateOf<KareError?>(null) }
    val onErrorDialogDismiss = {
        showErrorDialog = false
        workoutDetailsViewModel.clearError() //Enters launched effect to update showErrorDialog...
    }

    LaunchedEffect(
        workoutDetailsState,
        deleteExerciseState,
        deleteWorkoutDetailsState,
        updateWorkoutDetailsState,
        startWorkoutState,
        createWorkoutState,
        favoriteWorkoutState,
        unfavoriteWorkoutState
    ) {
        val states = listOf(
            workoutDetailsState,
            deleteExerciseState,
            deleteWorkoutDetailsState,
            updateWorkoutDetailsState,
            startWorkoutState,
            createWorkoutState,
            favoriteWorkoutState,
            unfavoriteWorkoutState
        )

        val errorState: BaseState = states.firstOrNull { it.isError } ?: BaseState()
        error = errorState.error
        showErrorDialog = errorState.isError
        Log.d("WorkoutDetailsScreen", "Error detected -> $showErrorDialog")

        val loadingState = states.firstOrNull { it.isLoading } ?: BaseState()
        showLoadingDialog = loadingState.isLoading
    }

    //Dialogs
    if (showErrorDialog) {
        Log.d("WorkoutDetailsScreen", "Error: $error")
        error?.let {
            ErrorDialog(it, onErrorDialogDismiss)
        }
    }

    if (showDeleteExerciseDialog) {
        DeleteExerciseDialog(
            onClick = onDeleteExercise,
            onDismiss = { showDeleteExerciseDialog = false }
        )
    }

    if (showDeleteMultipleExercisesDialog) {
        DeleteMultipleExercisesDialog(
            totalExercises = selectedExercises.size,
            onClick = onDeleteMultipleExercises,
            onDismiss = { showDeleteMultipleExercisesDialog = false }
        )
    }

    if (showEditWorkoutNameDialog) {
        EditWorkoutDialog(
            currentName = selectedWorkout.name,
            onDismiss = { showEditWorkoutNameDialog = false },
            onConfirm = onEditWorkoutName
        )
    }

    if (showDeleteWorkoutDialog) {
        WarningDialog(
            title = "Delete Workout",
            description = "Are you sure you want to delete this workout? This action cannot be undone.",
            positiveButtonTitle = "Delete",
            onClick = onDeleteWorkout,
            onDismiss = { showDeleteWorkoutDialog = false }
        )
    }


    if (showWorkoutConfigureDialog) {
        WorkoutConfigurationDialog(
            workoutConfiguration = workoutDetailsState.workoutDetails.configuration,
            onSave = onUpdateWorkoutConfiguration,
            onDismiss = { showWorkoutConfigureDialog = false }
        )
    }

    if (showFavoriteDialog) {
        FavoriteWorkoutDialog(actionTitle = "Favorite Workout",
            onClick = onFavoriteWorkout,
            onDismiss = { showFavoriteDialog = false }
        )
    }

    if (showUnfavoriteDialog) {
        FavoriteWorkoutDialog(actionTitle = "Unfavorite Workout",
            onClick = onUnfavoriteWorkout,
            onDismiss = {
                showUnfavoriteDialog = false
            }
        )
    }

    //Pull to refresh
    val pullRefreshState = rememberPullRefreshState(
        refreshing = workoutDetailsViewModel.isRefreshing,
        onRefresh = { workoutDetailsViewModel.getWorkoutDetails(workoutDetailsState.workoutDetails.workoutId) }
    )

    MainScreenScaffold(
        screenTitle = "",
        onNavigateToDashboard = { workoutDetailsViewModel.onNavigateToDashboard() },
        onNavigateToWorkouts = { workoutDetailsViewModel.onNavigateToWorkouts() },
        onNavigateBackAction = { workoutDetailsViewModel.onNavigateBack() },
        onNavigateToSettings = { workoutDetailsViewModel.onNavigateToSettings() },
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {

            if (showLoadingDialog) {
                LoadingWheel(innerPadding = innerPadding)
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {

                    WorkoutConfigurationHeader(
                        workout = selectedWorkout,
                        onConfigure = { showWorkoutConfigureDialog = true },
                        onDeleteWorkout = { showDeleteWorkoutDialog = true },
                        onEditWorkoutName = { showEditWorkoutNameDialog = true },
                        onFavoriteWorkout = { showFavoriteDialog = true },
                        onUnfavoriteWorkout = { showUnfavoriteDialog = true },
                        onAddExercise = { workoutDetailsViewModel.navigateToSearchExercises(selectedWorkout.workoutId) },
                        onStartWorkout = { workoutDetailsViewModel.startWorkout() }
                    )

                    LazyColumn {
                        items(selectedWorkout.exercises.size) {
                            val currentExercise = selectedWorkout.exercises[it]

                            ExerciseBannerV2(
                                exercise = currentExercise,
                                showDifficulty = true,
                                onClick = { onExerciseSelected(currentExercise) }
                            )
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



