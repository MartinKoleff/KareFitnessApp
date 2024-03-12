package com.koleff.kare_android.ui.compose.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koleff.kare_android.common.MockupDataGenerator
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.compose.components.ExerciseSetRow
import com.koleff.kare_android.ui.compose.components.LoadingWheel
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.ExerciseDetailsConfiguratorScaffold
import com.koleff.kare_android.ui.compose.dialogs.ErrorDialog
import com.koleff.kare_android.ui.event.OnExerciseUpdateEvent
import com.koleff.kare_android.ui.state.ExerciseState
import com.koleff.kare_android.ui.state.WorkoutDetailsState
import com.koleff.kare_android.ui.view_model.ExerciseDetailsConfiguratorViewModel

@Composable
fun ExerciseDetailsConfiguratorScreen(
    exerciseDetailsConfiguratorViewModel: ExerciseDetailsConfiguratorViewModel = hiltViewModel()
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val exerciseState by exerciseDetailsConfiguratorViewModel.exerciseState.collectAsState()
    val selectedWorkoutState by exerciseDetailsConfiguratorViewModel.selectedWorkoutState.collectAsState()
    val updateWorkoutState by exerciseDetailsConfiguratorViewModel.updateWorkoutState.collectAsState()

    Log.d("ExerciseDetailsConfiguratorScreen", exerciseState.exercise.muscleGroup.toString())
    val initialMuscleGroup = exerciseDetailsConfiguratorViewModel.initialMuscleGroup
    val exerciseImageId = MuscleGroup.getImage(initialMuscleGroup)

    val onSubmitExercise: () -> Unit = {
        if (!exerciseState.isLoading) {
            exerciseDetailsConfiguratorViewModel.onExerciseUpdateEvent(
                OnExerciseUpdateEvent.OnExerciseSubmit(exerciseState.exercise)
            )
        }
    }

    LaunchedEffect(updateWorkoutState) {

        //Await update workout
        if (updateWorkoutState.isSuccessful) {
            exerciseDetailsConfiguratorViewModel.navigateToWorkoutDetails(selectedWorkoutState.workoutDetails.workoutId)
        }
    }

    //Navigation Callbacks
    val onNavigateBack: () -> Unit = {
        exerciseDetailsConfiguratorViewModel.onNavigationEvent(
            NavigationEvent.NavigateBack
        )
    }

    //Dialog visibility
    var showErrorDialog by remember { mutableStateOf(false) }

    //Dialog callbacks
    val onErrorDialogDismiss = {
        showErrorDialog = false
        exerciseDetailsConfiguratorViewModel.clearError() //Enters launched effect to update showErrorDialog...
    }

    //Error handling
    var error by remember { mutableStateOf<KareError?>(null) }
    LaunchedEffect(
        exerciseState.isError,
        selectedWorkoutState.isError,
        updateWorkoutState.isError
    ) {

        error = if (exerciseState.isError) {
            exerciseState.error
        } else if (selectedWorkoutState.isError) {
            selectedWorkoutState.error
        } else if (updateWorkoutState.isError) {
            updateWorkoutState.error
        } else {
            null
        }

        showErrorDialog =
            exerciseState.isError || selectedWorkoutState.isError || updateWorkoutState.isError

        Log.d("ExerciseDetailsConfiguratorScreen", "Error detected -> $showErrorDialog")
    }

    //Dialogs
    if (showErrorDialog) {
        error?.let {
            ErrorDialog(error!!, onErrorDialogDismiss)
        }
    }

    ExerciseDetailsConfiguratorScaffold(
        screenTitle = exerciseState.exercise.name,
        exerciseImageId = exerciseImageId,
        onSubmitExercise = onSubmitExercise,
        onNavigateBackAction = onNavigateBack
    ) { innerPadding ->
        val modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .pointerInput(Unit) {

                //Hide keyboard on tap outside SearchBar
                detectTapGestures(
                    onTap = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                )
            }
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary
                    )
                )
            )

        ExerciseDetailsConfiguratorContent(
            modifier = modifier,
            exerciseState = exerciseState,
            updateWorkoutState = updateWorkoutState
        )
    }
}

@Composable
fun ExerciseDetailsConfiguratorContent(
    modifier: Modifier,
    exerciseState: ExerciseState,
    updateWorkoutState: WorkoutDetailsState,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            if (exerciseState.isLoading || updateWorkoutState.isLoading) {
                LoadingWheel()
            } else {
                Box(
                    modifier = Modifier
                        .size((50 + 8 + 8).dp)
                )
            }
        }
        item { //TODO: fix in place...
            Text(
                modifier = Modifier.padding(
                    PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = 8.dp
                    )
                ),
                text = exerciseState.exercise.name,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        //Rows with sets / reps / weight configuration
        items(exerciseState.exercise.sets.size) { currentSetId ->
            val currentSet = exerciseState.exercise.sets[currentSetId]
            ExerciseSetRow(
                set = currentSet,
                onRepsChanged = { newReps ->
                    currentSet.reps = newReps
//                    currentSet.setId = null //When set is changed -> generate new UUID
                },
                onWeightChanged = { newWeight ->
                    currentSet.weight = newWeight
//                    currentSet.setId = null //When set is changed -> generate new UUID
                }
            )
        }
    }
}

@Preview
@Composable
fun ExerciseDetailsConfiguratorScreenPreview() {
    val exerciseState = ExerciseState(
        exercise = MockupDataGenerator.generateExercise(),
        isSuccessful = true
    )

    val exerciseImageId = MuscleGroup.getImage(exerciseState.exercise.muscleGroup)

    ExerciseDetailsConfiguratorScaffold(
        screenTitle = exerciseState.exercise.name,
        exerciseImageId = exerciseImageId,
        onSubmitExercise = {},
        onNavigateBackAction = {}
    ) { innerPadding ->
        val modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary
                    )
                )
            )

        val updateWorkoutState = WorkoutDetailsState()
        ExerciseDetailsConfiguratorContent(
            modifier = modifier,
            exerciseState = exerciseState,
            updateWorkoutState = updateWorkoutState
        )
    }
}
