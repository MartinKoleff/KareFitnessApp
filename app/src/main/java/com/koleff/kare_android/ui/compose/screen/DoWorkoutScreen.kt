package com.koleff.kare_android.ui.compose.screen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SheetState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koleff.kare_android.R
import com.koleff.kare_android.common.MockupDataGenerator
import com.koleff.kare_android.common.TimerUtil
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.ExerciseTime
import com.koleff.kare_android.ui.compose.components.ExerciseDataSheet
import com.koleff.kare_android.ui.compose.components.ExerciseTimer
import com.koleff.kare_android.ui.compose.components.LoadingWheel
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.DoWorkoutScaffold
import com.koleff.kare_android.ui.compose.dialogs.ErrorDialog
import com.koleff.kare_android.ui.state.ExerciseTimerStyle
import com.koleff.kare_android.ui.view_model.DoWorkoutViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DoWorkoutScreen(doWorkoutViewModel: DoWorkoutViewModel = hiltViewModel()) {
    val state by doWorkoutViewModel.state.collectAsState()

    val time =
        state.doWorkoutData.defaultExerciseTime
    val currentExercise = state.doWorkoutData.currentExercise
    val currentSet = state.doWorkoutData.currentSetNumber

    var timeLeft by remember {
        mutableStateOf(time)
    }

    //Navigation Callbacks
    val onExitWorkoutAction = {
        doWorkoutViewModel.onNavigationEvent(
            NavigationEvent.ClearBackstackAndNavigateTo(Destination.Dashboard)
        )
    }

    val onNextExerciseAction = {
        Log.d("DoWorkoutScreen", "Select next exercise requested.")
        doWorkoutViewModel.selectNextExercise()

        //Reset timer...
        timeLeft = time //TODO: test for await...
    }

    //Observe selectNextExercise
    var showNextExerciseCountdown by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(state.doWorkoutData.isNextExerciseCountdown) {
        showNextExerciseCountdown = state.doWorkoutData.isNextExerciseCountdown
        Log.d("DoWorkoutScreen", "Show next exercise countdown: $showNextExerciseCountdown")
    }

    //Wait for data on initialization
    var showErrorDialog by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(state.isError) {
        showErrorDialog = state.isError
        Log.d("DoWorkoutScreen", "Error: $showErrorDialog")
    }

    //Dialog callbacks
    val onErrorDialogDismiss = {
        showErrorDialog = false
        doWorkoutViewModel.clearError() //Enters launched effect to update showErrorDialog...
    }

    //Wait for data on initialization
    var showLoadingDialog by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(state.isLoading) {
        showLoadingDialog = state.isLoading
        Log.d("DoWorkoutScreen", "Loading: $showLoadingDialog")
    }

    //Error dialog
    if (showErrorDialog) {
        ErrorDialog(state.error, onErrorDialogDismiss)
    }

    //Lower alpha for when NextExerciseCountdownScreen is displayed...
    val screenModifier = if (showNextExerciseCountdown) {
        Modifier
            .fillMaxSize()
            .alpha(0.15f)
    } else Modifier.fillMaxSize()

    //Loading screen
    if (showLoadingDialog) {
        LoadingWheel()
    } else {
        DoWorkoutScaffold(
            modifier = screenModifier,
            screenTitle = currentExercise.name,
            onExitWorkoutAction = onExitWorkoutAction,
            onNextExerciseAction = onNextExerciseAction
        ) {

            //Video player...

            DoWorkoutFooterWithModal(
                totalTime = time,
                exercise = currentExercise,
                currentSet = currentSet,
                defaultTotalSets = state.doWorkoutData.defaultTotalSets
            ) {
                timeLeft = it

                //Exercise completed. Select next exercise.
                if (timeLeft == ExerciseTime(0, 0, 0)) {
                    onNextExerciseAction()
                }
            }
        }

        //Next exercise countdown screen overlay
        if (showNextExerciseCountdown) { //TODO: add animation transition for more smooth appearance...
            NextExerciseCountdownScreen(
                nextExercise = state.doWorkoutData.currentExercise,
                currentSetNumber = state.doWorkoutData.currentSetNumber,
                countdownTime = state.doWorkoutData.countdownTime,
                defaultTotalSets = state.doWorkoutData.defaultTotalSets
            ) { countdownTime ->

                //Start next exercise...
                if (countdownTime == ExerciseTime(0, 0, 0)) {
                    doWorkoutViewModel.confirmExercise()
//                    showNextExerciseCountdown = false
                }
            }
        }
    }
}

//Cant be separate screen because of navigation delay...
//Rather be overlay on the existing DoWorkoutScreen blurring the background until it gets configured (next exercise, reset timer, etc...)
@Composable
fun NextExerciseCountdownScreen(
    nextExercise: ExerciseDto,
    currentSetNumber: Int,
    isWorkoutComplete: Boolean = false,
    countdownTime: ExerciseTime,
    defaultTotalSets: Int,
    onTimePassed: (ExerciseTime) -> Unit
) {
    var time by remember {
        mutableStateOf(countdownTime)
    }

    //On screen initialization...
    LaunchedEffect(Unit) {
        Log.d("NextExerciseCountdownScreen", "Timer started.")
        TimerUtil.startTimer(countdownTime.toSeconds()) {
            time = it
            onTimePassed(it)
        }
    }

    val alpha = 0.5f
    val motivationalQuote = MockupDataGenerator.generateMotivationalQuote()
    val totalSets = if(nextExercise.sets.isNotEmpty()) nextExercise.sets.size else defaultTotalSets
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
        val textColor = Color.White
        val exerciseText = if (isWorkoutComplete) {
            "Workout completed!"
        } else {
            "Next exercise: ${nextExercise.name}."
        }

        val setText =
            "Currently doing set $currentSetNumber out of $totalSets total sets."

        //Texts
        Text(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 6.dp),
            text = exerciseText,
            style = TextStyle(
                color = textColor,
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold
            ),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 6.dp),
            text = setText,
            style = TextStyle(
                color = textColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            ),
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
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 6.dp),
                text = time.toSeconds().toString(),
                style = TextStyle(
                    color = textColor,
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Medium
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
fun NextExerciseCountdownScreenPreview() {
    val nextExercise = MockupDataGenerator.generateExercise()
    val currentSetNumber = 2
    val countdownTime = ExerciseTime(hours = 0, minutes = 0, seconds = 10)
    val defaultTotalSets = 4
    val onTimePassed: (ExerciseTime) -> Unit = {}
    NextExerciseCountdownScreen(
        nextExercise = nextExercise,
        currentSetNumber = currentSetNumber,
        countdownTime = countdownTime,
        defaultTotalSets = defaultTotalSets,
        onTimePassed = onTimePassed
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DoWorkoutFooterWithModal(
    totalTime: ExerciseTime,
    exercise: ExerciseDto,
    currentSet: Int,
    defaultTotalSets: Int,
    onTimePassed: (ExerciseTime) -> Unit
) {
    ExerciseDataSheetModal2(exercise = exercise, currentSet = currentSet, defaultTotalSets = defaultTotalSets) {

        //Footer
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.BottomCenter
        ) {
            DoWorkoutFooter(totalTime = totalTime, onTimePassed = onTimePassed)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun DoWorkoutFooterWithModalPreview() {
    val time = ExerciseTime(hours = 0, minutes = 1, seconds = 30)
    val exercise = MockupDataGenerator.generateExercise()
    val currentSet = 1
    val defaultTotalSets = 4
    val onTimePassed: (ExerciseTime) -> Unit = {

    }

    DoWorkoutFooterWithModal(
        totalTime = time,
        exercise = exercise,
        currentSet = currentSet,
        defaultTotalSets = defaultTotalSets,
        onTimePassed = onTimePassed
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DoWorkoutFooter(
    exerciseTimerStyle: ExerciseTimerStyle = ExerciseTimerStyle(),
    totalTime: ExerciseTime,
    onTimePassed: (ExerciseTime) -> Unit
) {
    val exerciseDataPadding = PaddingValues(4.dp)
    val textColor = Color.White

    //Timer
    val hours = totalTime.hours
    val minutes = totalTime.minutes
    val seconds = totalTime.seconds
    var currentTime by remember {
        mutableStateOf(
            String.format(
                "%02d:%02d:%02d",
                hours,
                minutes,
                seconds
            )
        )
    }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(exerciseTimerStyle.timerRadius * 1.5f)
    ) {

        //reps
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(
                    exerciseDataPadding
                ),
                text = "8",
                style = TextStyle(
                    color = textColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.padding(
                    exerciseDataPadding
                ),
                text = "Reps",
                style = TextStyle(
                    color = textColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        //timer
        LaunchedEffect(Unit) {
            TimerUtil.startTimer(totalTime.toSeconds()) {
                onTimePassed(it)

                currentTime = it.toString()
            }
        }

        ExerciseTimer(
            modifier = Modifier
                .fillMaxHeight()
                .weight(2.5f),
            timeLeft = currentTime,
            totalTime = totalTime
        )

        //weight
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(
                    exerciseDataPadding
                ),
                text = "--",
                style = TextStyle(
                    color = textColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.padding(
                    exerciseDataPadding
                ),
                text = "Weight",
                style = TextStyle(
                    color = textColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun DoWorkoutFooterPreview() {
    val time = ExerciseTime(hours = 0, minutes = 1, seconds = 30)
    val exercise = MockupDataGenerator.generateExercise()
    val onTimePassed: (ExerciseTime) -> Unit = {

    }
    DoWorkoutFooter(totalTime = time, onTimePassed = onTimePassed)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDataSheetModal() {
    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }

    if (isSheetOpen) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                isSheetOpen = false
            },
            dragHandle = {
                GrabHandle()
            }
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_default),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun ExerciseDataSheetModalPreview() {
    ExerciseDataSheetModal()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDataSheetModal2(
    exercise: ExerciseDto,
    currentSet: Int,
    defaultTotalSets: Int,
    content: @Composable (paddingValues: PaddingValues) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val sheetPeekHeight = 88.dp

    val scaffoldState = rememberBottomSheetScaffoldState()
    BottomSheetScaffold(
        modifier = Modifier
            .fillMaxWidth(),
        scaffoldState = scaffoldState,
        sheetContent = {
            CurrentExerciseInfoRow(
                currentExercise = exercise,
                currentSet = currentSet,
                defaultTotalSets = defaultTotalSets
            )

            ExerciseDataSheet(exercise = exercise)
        },
        sheetDragHandle = {
            GrabHandle()
        },
        sheetPeekHeight = sheetPeekHeight
    ) {

        //Screen
        content(it)
    }
}

@Composable
fun CurrentExerciseInfoRow(currentExercise: ExerciseDto, currentSet: Int, defaultTotalSets: Int) {
    val textColor = Color.White
    val setsTextColor = Color.Yellow
    val cornerSize = 24.dp

    val totalSets = if(currentExercise.sets.isNotEmpty()) currentExercise.sets.size else defaultTotalSets

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(
                RoundedCornerShape(cornerSize)
            )
            .border(
                border = BorderStroke(2.dp, color = Color.White),
                shape = RoundedCornerShape(cornerSize)
            )
            .background(Color.Gray),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier
                .padding(
                    horizontal = 8.dp
                )
                .weight(5f),
            text = currentExercise.name,
            style = TextStyle(
                color = textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            modifier = Modifier
                .padding(
                    horizontal = 8.dp
                )
                .weight(1f),
            text = "$currentSet of $totalSets",
            style = TextStyle(
                color = setsTextColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
fun CurrentExerciseInfoRowInfoRowPreview() {
    val currentExercise = MockupDataGenerator.generateExercise()
    val currentSet = 1
    val defaultTotalSets = 4
    CurrentExerciseInfoRow(
        currentExercise = currentExercise,
        currentSet = currentSet,
        defaultTotalSets = defaultTotalSets
    )
}

@Preview
@Composable
fun ExerciseDataSheetModal2Preview() {
    val exercise = MockupDataGenerator.generateExercise()
    val currentSet = 1
    val defaultTotalSets = 4
    ExerciseDataSheetModal2(
        exercise = exercise,
        currentSet = currentSet,
        defaultTotalSets = defaultTotalSets
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        )
    }
}

@Composable
fun GrabHandle(modifier: Modifier = Modifier) {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    Box(
        modifier = modifier
            .width(screenWidth / 2 - 75.dp) //.fillMaxWidth()
            .padding(16.dp)
            .height(5.dp)
            .background(
                color = Color.LightGray,
                shape = RoundedCornerShape(50)
            )
    )
}

@Preview
@Composable
fun GrabHandlePreview() {
    GrabHandle()
}
