package com.koleff.kare_android.ui.compose.screen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koleff.kare_android.R
import com.koleff.kare_android.common.MockupDataGeneratorV2
import com.koleff.kare_android.common.timer.TimerUtil
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.ExerciseProgressDto
import com.koleff.kare_android.data.model.dto.ExerciseSetDto
import com.koleff.kare_android.data.model.dto.ExerciseTime
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.compose.components.DoWorkoutFooter
import com.koleff.kare_android.ui.compose.components.ExerciseDataSheet
import com.koleff.kare_android.ui.compose.components.ExerciseDataSheetModal2
import com.koleff.kare_android.ui.compose.components.ExerciseTimer
import com.koleff.kare_android.ui.compose.components.LoadingWheel
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.DoWorkoutScaffold
import com.koleff.kare_android.ui.compose.dialogs.ErrorDialog
import com.koleff.kare_android.ui.compose.dialogs.ExitWorkoutDialog
import com.koleff.kare_android.ui.compose.dialogs.WorkoutCompletedDialog
import com.koleff.kare_android.ui.state.ExerciseTimerStyle
import com.koleff.kare_android.ui.view_model.DoWorkoutViewModel
import kotlin.random.Random

//TODO: show paused icon on pause timer...
//TODO: show resume icon on resume timer... with 3 seconds countdown?

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DoWorkoutScreen(doWorkoutViewModel: DoWorkoutViewModel = hiltViewModel()) {
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
    } else Modifier
        .fillMaxSize()
        .clickable {
            doWorkoutViewModel
                .onScreenClick()
                .also {
                    isPaused = !isPaused

                    doWorkoutViewModel.showPlayerOverlay()
                } //Pause/Resume click listener
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

                //Video player...
                //TODO: video player of current exercise...

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
            if (showNextExerciseCountdown) { //TODO: add animation transition for more smooth appearance...
                Log.d(
                    "DoWorkoutScreen",
                    "Workout timer = ${workoutTimerState.time}. Countdown timer: ${countdownTimerState.time}"
                )

                NextExerciseCountdownScreen(
                    nextExercise = if (state.doWorkoutData.isNextExercise) state.doWorkoutData.nextExercise else state.doWorkoutData.currentExercise,
                    currentSetNumber = state.doWorkoutData.nextSetNumber,
                    countdownTime = countdownTimerState.time,
                    defaultTotalSets = state.doWorkoutData.defaultTotalSets,
                    countdownNumberPadding = PaddingValues(
                        top = exerciseDataSheetPaddingValues.calculateTopPadding(),
                        bottom = exerciseDataSheetPaddingValues.calculateBottomPadding() + 88.dp,
                        start = exerciseDataSheetPaddingValues.calculateStartPadding(LayoutDirection.Ltr),
                        end = exerciseDataSheetPaddingValues.calculateEndPadding(LayoutDirection.Ltr)
                    ) //sheetPeekHeight = 88.dp //exerciseDataSheetPaddingValues
                )
            }
        }

        if (showPlayerOverlay) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.size(75.dp),
                    painter = painterResource(
                        if (isPaused) R.drawable.ic_pause else R.drawable.ic_resume
                    ),
                    contentDescription = "Pause/Resume",
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

//Cant be separate screen because of navigation delay...
//Rather be overlay on the existing DoWorkoutScreen blurring the background until it gets configured
// (next exercise, reset timer, etc...)
@Composable
fun NextExerciseCountdownScreen(
    nextExercise: ExerciseDto,
    currentSetNumber: Int,
    isWorkoutComplete: Boolean = false,
    countdownTime: ExerciseTime,
    defaultTotalSets: Int,
    countdownNumberPadding: PaddingValues = PaddingValues(vertical = 12.dp, horizontal = 6.dp)
) {
    val alpha = 0.5f
    val motivationalQuote = MockupDataGeneratorV2.generateMotivationalQuote()
    val totalSets = if (nextExercise.sets.isNotEmpty()) nextExercise.sets.size else defaultTotalSets
    Column(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                val colors = listOf(
                    Color.Red.copy(alpha = alpha),
                    Color.Red.copy(alpha = alpha),
                    Color.Red.copy(alpha = alpha),
                    Color.Blue.copy(alpha = alpha),
                    Color.Blue.copy(alpha = alpha),
                    Color.Cyan.copy(alpha = alpha),
                    Color.Yellow.copy(alpha = alpha)
                )
                drawRect(
                    brush = Brush.linearGradient(colors),
                    blendMode = BlendMode.Darken
                )
            },
        verticalArrangement = Arrangement.Top
    ) {
        val exerciseText = if (isWorkoutComplete) {
            "Workout completed!"
        } else {
            "Next exercise: ${nextExercise.name}."
        }

        val setText =
            "Currently doing set $currentSetNumber out of $totalSets total sets."

        val textColor = MaterialTheme.colorScheme.onSurface
        val titleTextStyle = MaterialTheme.typography.displaySmall.copy(
            color = textColor
        )

        val subtitleTextStyle = MaterialTheme.typography.headlineSmall.copy(
            color = textColor
        )

        val timerTextStyle = MaterialTheme.typography.displayLarge.copy(
            color = textColor
        )

        //Texts
        Text(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 6.dp),
            text = exerciseText,
            style = titleTextStyle,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 6.dp),
            text = setText,
            style = subtitleTextStyle,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        //Time countdown until next exercise
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(7f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(countdownNumberPadding),
                text = countdownTime.toSeconds().toString(),
                style = timerTextStyle,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
fun NextExerciseCountdownScreenPreview() {
    val nextExercise = MockupDataGeneratorV2.generateExercise()
    val currentSetNumber = 2
    val countdownTime = ExerciseTime(hours = 0, minutes = 0, seconds = 10)
    val defaultTotalSets = 4
    val onTimePassed: (ExerciseTime) -> Unit = {}
    val countdownTimer = TimerUtil(countdownTime.toSeconds())
    NextExerciseCountdownScreen(
        nextExercise = nextExercise,
        currentSetNumber = currentSetNumber,
        countdownTime = countdownTime,
        defaultTotalSets = defaultTotalSets
    )
}
