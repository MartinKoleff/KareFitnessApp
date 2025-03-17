package com.koleff.kare_android.ui.compose.screen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.compose.components.CurrentSetFooter
import com.koleff.kare_android.ui.compose.components.NumberOfRepsBox
import com.koleff.kare_android.ui.compose.components.NumberOfSetsBox
import com.koleff.kare_android.ui.compose.components.SaveExerciseConfigurationButton
import com.koleff.kare_android.ui.compose.components.WeightBox
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.MainScreenScaffold
import com.koleff.kare_android.ui.compose.dialogs.ErrorDialog
import com.koleff.kare_android.ui.compose.dialogs.LoadingDialog
import com.koleff.kare_android.ui.event.OnExerciseUpdateEvent
import com.koleff.kare_android.ui.state.BaseState
import com.koleff.kare_android.ui.view_model.ExerciseConfiguratorViewModel

@Composable
fun ExerciseConfiguratorScreen(
    exerciseConfiguratorViewModel: ExerciseConfiguratorViewModel = hiltViewModel()
) {
    val exerciseState by exerciseConfiguratorViewModel.exerciseState.collectAsState()
    val workoutState by exerciseConfiguratorViewModel.workoutState.collectAsState()
    val updateWorkoutState by exerciseConfiguratorViewModel.updateWorkoutState.collectAsState()
    val currentSetIndex by exerciseConfiguratorViewModel.currentEditedSetIndex.collectAsState()

    Log.d("ExerciseConfiguratorScreen", "Muscle group : ${exerciseState.exercise.muscleGroup}")
    val initialMuscleGroup = exerciseConfiguratorViewModel.initialMuscleGroup
    val exerciseImageId = MuscleGroup.getImage(initialMuscleGroup)

    LaunchedEffect(updateWorkoutState) {

        //Await update workout
        if (updateWorkoutState.isSuccessful) {
            exerciseConfiguratorViewModel.navigateToWorkoutDetails(workoutState.workoutDetails.workoutId)
        }
    }
    //Dialog callbacks
    val onSubmitExercise: () -> Unit = {
        if (!exerciseState.isLoading) {

            //Replace old exercise with new
            exerciseConfiguratorViewModel.onExerciseUpdateEvent(
                OnExerciseUpdateEvent.OnExerciseSubmit(exerciseState.exercise)
            )
        }
    }

    //Dialog visibility
    var showErrorDialog by remember { mutableStateOf(false) }
    var showLoadingDialog by remember { mutableStateOf(false) }

    //Error handling
    var error by remember { mutableStateOf<KareError?>(null) }
    val onErrorDialogDismiss = {
        showErrorDialog = false
        exerciseConfiguratorViewModel.clearError() //Enters launched effect to update showErrorDialog...
    }
    LaunchedEffect(
        exerciseState,
        workoutState,
        updateWorkoutState
    ) {
        val states = listOf(
            exerciseState,
            workoutState,
            updateWorkoutState
        )

        val errorState: BaseState = states.firstOrNull { it.isError } ?: BaseState()
        error = errorState.error
        showErrorDialog = errorState.isError
        Log.d("ExerciseConfiguratorScreen", "Error detected -> $showErrorDialog")

        val loadingState: BaseState = states.firstOrNull { it.isLoading } ?: BaseState()
        showLoadingDialog = loadingState.isLoading
    }

    //Dialogs
    if (showErrorDialog) {
        error?.let {
            ErrorDialog(it, onErrorDialogDismiss)
        }
    }

    if (showLoadingDialog) {
        LoadingDialog(onDismiss = {
            showLoadingDialog = false
        })
    }

    MainScreenScaffold(
        screenTitle = exerciseState.exercise.name,
        onNavigateToDashboard = { exerciseConfiguratorViewModel.onNavigateToDashboard() },
        onNavigateToWorkouts = { exerciseConfiguratorViewModel.onNavigateToWorkouts() },
        onNavigateBackAction = { exerciseConfiguratorViewModel.onNavigateBack() },
        onNavigateToSettings = { exerciseConfiguratorViewModel.onNavigateToSettings() },
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NumberOfSetsBox(
                totalSets = exerciseState.exercise.sets.size,
                onDeleteSet = {
                    exerciseConfiguratorViewModel.deleteLatestSet()
                },
                onAddSet = {
                    exerciseConfiguratorViewModel.addNewSet()
                }
            )
            Spacer(modifier = Modifier.height(12.dp))

            CurrentSetFooter(
                currentSetIndex = currentSetIndex,
                increaseCurrentEditedSet = { exerciseConfiguratorViewModel.onSetChange(isIncrease = true) },
                decreaseCurrentEditedSet = { exerciseConfiguratorViewModel.onSetChange(isIncrease = false) }
            )
            Spacer(modifier = Modifier.height(12.dp))

            NumberOfRepsBox(
                totalReps = exerciseConfiguratorViewModel.currentEditedSet.reps,
                onDecreaseReps = {
                    exerciseConfiguratorViewModel.onDecreaseReps()
                },
                onIncreaseReps = {
                    exerciseConfiguratorViewModel.onIncreaseReps()
                }
            )
            Spacer(modifier = Modifier.height(12.dp))

            WeightBox(
                weight = exerciseConfiguratorViewModel.currentEditedSet.weight,
                onIncreaseWeight = { newWeight ->
                    exerciseConfiguratorViewModel.onIncreaseWeight(newWeight)
                },
                onDecreaseWeight = { newWeight ->
                    exerciseConfiguratorViewModel.onDecreaseWeight(newWeight)
                }
            )
            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp, bottom = 24.dp)
            ) {
                SaveExerciseConfigurationButton(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    onClick = onSubmitExercise
                )
            }
        }
    }
}
