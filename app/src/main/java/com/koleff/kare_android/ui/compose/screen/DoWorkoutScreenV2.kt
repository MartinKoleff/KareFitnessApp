package com.koleff.kare_android.ui.compose.screen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.compose.components.DoWorkoutFooter
import com.koleff.kare_android.ui.compose.components.ExerciseDataSheetModal2
import com.koleff.kare_android.ui.compose.components.LoadingWheel
import com.koleff.kare_android.ui.compose.components.NextExerciseInfoScreen
import com.koleff.kare_android.ui.compose.components.PauseScreenOverlay
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.DoWorkoutScaffold
import com.koleff.kare_android.ui.compose.dialogs.ErrorDialog
import com.koleff.kare_android.ui.compose.dialogs.ExitWorkoutDialog
import com.koleff.kare_android.ui.compose.dialogs.WorkoutCompletedDialog
import com.koleff.kare_android.ui.view_model.DoWorkoutViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DoWorkoutScreenV2(doWorkoutViewModel: DoWorkoutViewModel = hiltViewModel()) {
    val state by doWorkoutViewModel.state.collectAsState()
    val workoutTimerState by doWorkoutViewModel.workoutTimerState.collectAsState()
    val countdownTimerState by doWorkoutViewModel.countdownTimerState.collectAsState()
    val playerState by doWorkoutViewModel.playerState.collectAsState()

    var workoutTimerInitialState by remember {
        mutableStateOf(workoutTimerState)
    }

    //Navigation Callbacks
    val onExitWorkoutAction = {
        doWorkoutViewModel.exitWorkout()
    }

    //Observe selectNextExercise
    var showNextExerciseCountdown by remember {
        mutableStateOf(false)
    }

    //Wait for data on initialization
    var showLoadingDialog by remember {
        mutableStateOf(state.isLoading)
    }

    var showWorkoutCompletedDialog by remember {
        mutableStateOf(false)
    }

    var showExitWorkoutDialog by remember {
        mutableStateOf(false)
    }

    //Wait for data on initialization
    var showErrorDialog by remember {
        mutableStateOf(false)
    }

    //Error handling
    var error: KareError? by remember {
        mutableStateOf(null)
    }

    //Dialog callbacks
    val onErrorDialogDismiss = {
        showErrorDialog = false
        doWorkoutViewModel.clearError() //Enters launched effect to update showErrorDialog...
    }
    LaunchedEffect(state) {
        showErrorDialog = state.isError
        error = state.error
        Log.d("DoWorkoutScreen", "Error: $showErrorDialog")

        showLoadingDialog = state.isLoading
        Log.d("DoWorkoutScreen", "Loading: $showLoadingDialog")

        showNextExerciseCountdown = state.doWorkoutData.isBetweenExerciseCountdown
        workoutTimerInitialState = workoutTimerState
        Log.d("DoWorkoutScreen", "Show next exercise: $showNextExerciseCountdown")

        showWorkoutCompletedDialog = state.doWorkoutData.isWorkoutCompleted
        Log.d("DoWorkoutScreen", "Is workout completed: $showWorkoutCompletedDialog")
    }

    var showPlayerOverlay by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }

    LaunchedEffect(playerState) {
        showPlayerOverlay = playerState.isLoading
    }


    //Error dialog
    if (showErrorDialog) {
        error?.let {
            ErrorDialog(it, onErrorDialogDismiss)
        }
    }

    //Workout completed dialog
    if (showWorkoutCompletedDialog) {
        WorkoutCompletedDialog(
            workoutName = state.doWorkoutData.workout.name,
            onClick = {
                doWorkoutViewModel.navigateToDashboard()

                showWorkoutCompletedDialog = false
            }
        )
    }

    if (showExitWorkoutDialog) {
        ExitWorkoutDialog(
            workoutName = state.doWorkoutData.workout.name,
            onClick = {
                onExitWorkoutAction()

                showExitWorkoutDialog = false
            },
            onDismiss = {
                showExitWorkoutDialog = false
            }
        )
    }

    //Blur for Android 12+ / Lower alpha for < Android 12  when NextExerciseCountdownScreen is displayed...
    val screenModifier = if (showNextExerciseCountdown) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Modifier
                .fillMaxSize()
                .blur(20.dp, 20.dp)
        } else {
            Modifier
                .fillMaxSize()
                .alpha(0.15f)
        }
    } else {
        Modifier
            .fillMaxSize()
            .clickable {
                if (!showNextExerciseCountdown) {
                    doWorkoutViewModel
                        .onScreenClick()
                        .also {
                            isPaused = !isPaused

                            doWorkoutViewModel.showPlayerOverlay()
                        } //Pause/Resume click listener
                }
            }
    }

    //Loading screen
    if (showLoadingDialog) {
        LoadingWheel()
    } else {

        //Above all screens
        ExerciseDataSheetModal2(
            exercise = state.doWorkoutData.currentExercise,
            currentSetNumber = state.doWorkoutData.currentSetNumber,
            defaultTotalSets = state.doWorkoutData.defaultTotalSets,
            isNextExercise = state.doWorkoutData.isNextExercise,
            onSaveExerciseData = { exerciseData ->
                doWorkoutViewModel.addDoWorkoutExerciseSet(exerciseData)
            }
        ) { exerciseDataSheetPaddingValues ->
            DoWorkoutScaffold(
                modifier = screenModifier,
                screenTitle = state.doWorkoutData.currentExercise.name,
                onExitWorkoutAction = {

                    //Disable exit workout button when NextExerciseCountdownScreen is visible
                    if (!state.doWorkoutData.isBetweenExerciseCountdown) {
                        showExitWorkoutDialog = true
                    }
                },
                onNextExerciseAction = {

                    //Disable skip next exercise button when NextExerciseCountdownScreen is visible
                    if (!state.doWorkoutData.isBetweenExerciseCountdown) {
                        doWorkoutViewModel.skipNextExercise()
                    }
                }
            ) {

                //Exercise Timer, weight and reps
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(exerciseDataSheetPaddingValues),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    DoWorkoutFooter(
                        totalTime = workoutTimerInitialState.time,
                        timeLeft = workoutTimerState.time,
                        currentSet = state.doWorkoutData.currentSet
                    )
                }
            }

            //Next exercise countdown screen overlay
            if (showNextExerciseCountdown) {
                Log.d(
                    "DoWorkoutScreen",
                    "Workout timer = ${workoutTimerState.time}. Countdown timer: ${countdownTimerState.time}"
                )

                NextExerciseInfoScreen(
                    nextExercise = if (state.doWorkoutData.isNextExercise) state.doWorkoutData.nextExercise else state.doWorkoutData.currentExercise,
                    set = state.doWorkoutData.nextSetNumber,
                    totalSets = state.doWorkoutData.totalSets,
                    weight = state.doWorkoutData.nextSet.weight,
                    reps = state.doWorkoutData.nextSet.reps,
                    countdownTime = countdownTimerState.time,
                    onSkipSet = {
                        doWorkoutViewModel.skipNextSet()
                    },
                    onPause = { doWorkoutViewModel.pauseTimer(false) },
                    onResume = { doWorkoutViewModel.resumeCountdownTimer() },
                )
            } else if (showPlayerOverlay) {
                PauseScreenOverlay(isPaused)
            }
        }
    }
}
