package com.koleff.kare_android.ui.compose.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.koleff.kare_android.common.MockupDataGenerator
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.ui.MainScreen
import com.koleff.kare_android.ui.compose.ExerciseSetRow
import com.koleff.kare_android.ui.compose.LoadingWheel
import com.koleff.kare_android.ui.compose.banners.openWorkoutDetailsScreen
import com.koleff.kare_android.ui.compose.scaffolds.ExerciseDetailsConfiguratorScreenScaffold
import com.koleff.kare_android.ui.event.OnExerciseUpdateEvent
import com.koleff.kare_android.ui.state.ExerciseState
import com.koleff.kare_android.ui.state.WorkoutDetailsState
import com.koleff.kare_android.ui.view_model.ExerciseDetailsConfiguratorViewModel

@Composable
fun ExerciseDetailsConfiguratorScreen(
    navController: NavHostController,
    isNavigationInProgress: MutableState<Boolean>,
    exerciseDetailsConfiguratorViewModel: ExerciseDetailsConfiguratorViewModel,
    initialMuscleGroupId: Int
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val exerciseState by exerciseDetailsConfiguratorViewModel.exerciseState.collectAsState()
    val selectedWorkoutState by exerciseDetailsConfiguratorViewModel.selectedWorkoutState.collectAsState()
    val updateWorkoutState by exerciseDetailsConfiguratorViewModel.updateWorkoutState.collectAsState()

    Log.d("ExerciseDetailsConfiguratorScreen", exerciseState.exercise.muscleGroup.toString())
    val exerciseImageId = MuscleGroup.getImage(MuscleGroup.fromId(initialMuscleGroupId))

    val onSubmitExercise: () -> Unit = {
        exerciseDetailsConfiguratorViewModel.onExerciseUpdateEvent(
            OnExerciseUpdateEvent.OnExerciseSubmit(exerciseState.exercise)
        )
    }

    LaunchedEffect(updateWorkoutState) {

        //Await update workout
        if (updateWorkoutState.isSuccessful) {

            navController.navigate(MainScreen.WorkoutDetails.createRoute(workoutId = selectedWorkoutState.workout.workoutId)) {

                //Pop backstack and set the first element to be the dashboard
                popUpTo(MainScreen.Dashboard.route) { inclusive = false }

                //Clear all other entries in the back stack
                launchSingleTop = true
            }
        }
    }

    ExerciseDetailsConfiguratorScreenScaffold(
        screenTitle = exerciseState.exercise.name,
        navController = navController,
        isNavigationInProgress = isNavigationInProgress,
        exerciseImageId = exerciseImageId,
        onSubmitExercise = onSubmitExercise
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
        if (exerciseState.isLoading || updateWorkoutState.isLoading) {
            item {
                LoadingWheel()
            }
        } else {
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
                ExerciseSetRow(set = currentSet)
            }
        }
    }
}

@Preview
@Composable
fun ExerciseDetailsConfiguratorScreenPreview() {
    val navController = rememberNavController()
    val isNavigationInProgress = mutableStateOf(false)
    val exerciseState = ExerciseState(
        exercise = MockupDataGenerator.generateExercise(),
        isSuccessful = true
    )

    val exerciseImageId = MuscleGroup.getImage(exerciseState.exercise.muscleGroup)

    ExerciseDetailsConfiguratorScreenScaffold(
        screenTitle = exerciseState.exercise.name,
        navController = navController,
        isNavigationInProgress = isNavigationInProgress,
        exerciseImageId = exerciseImageId,
        onSubmitExercise = {}
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
