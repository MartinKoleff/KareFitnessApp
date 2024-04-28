package com.koleff.kare_android.ui.compose.screen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.koleff.kare_android.common.MockupDataGenerator
import com.koleff.kare_android.common.timer.TimerUtil
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.ExerciseProgressDto
import com.koleff.kare_android.data.model.dto.ExerciseSetDto
import com.koleff.kare_android.data.model.dto.ExerciseTime
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.compose.components.ExerciseDataSheet
import com.koleff.kare_android.ui.compose.components.ExerciseTimer
import com.koleff.kare_android.ui.compose.components.LoadingWheel
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.DoWorkoutScaffold
import com.koleff.kare_android.ui.compose.dialogs.ErrorDialog
import com.koleff.kare_android.ui.compose.dialogs.WorkoutCompletedDialog
import com.koleff.kare_android.ui.state.ExerciseTimerStyle
import com.koleff.kare_android.ui.view_model.DoWorkoutViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DoWorkoutScreen(doWorkoutViewModel: DoWorkoutViewModel = hiltViewModel()) {
    val state by doWorkoutViewModel.state.collectAsState()

    val workoutTimerState by doWorkoutViewModel.workoutTimerState.collectAsState()
    val countdownTimerState by doWorkoutViewModel.countdownTimerState.collectAsState()
    val currentExercise = state.doWorkoutData.currentExercise
    val currentSetNumber = state.doWorkoutData.currentSetNumber
    val currentSet = state.doWorkoutData.currentSet
    val nextExercise = state.doWorkoutData.nextExercise
    val nextSetNumber = state.doWorkoutData.nextSetNumber
    val nextSet = state.doWorkoutData.nextSet

    var workoutTimerInitialState by remember {
        mutableStateOf(workoutTimerState)
    }

    //Navigation Callbacks
    val onExitWorkoutAction = {
        doWorkoutViewModel.onNavigationEvent(
            NavigationEvent.ClearBackstackAndNavigateTo(Destination.Dashboard)
        )
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

    //Error dialog
    if (showErrorDialog) {
        error?.let {
            ErrorDialog(it, onErrorDialogDismiss)
        }
    }

    //Workout completed dialog
    if(showWorkoutCompletedDialog){
        WorkoutCompletedDialog(
            workoutName = state.doWorkoutData.workout.name,
            onClick = onExitWorkoutAction
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
    } else Modifier.fillMaxSize()

    //Loading screen
    if (showLoadingDialog) {
        LoadingWheel()
    } else {

        //Above all screens
        ExerciseDataSheetModal2(
            exercise = currentExercise,
            currentSetNumber = currentSetNumber,
            defaultTotalSets = state.doWorkoutData.defaultTotalSets,
            isNextExercise = state.doWorkoutData.isNextExercise,
            onSaveExerciseData = { exerciseData ->
                doWorkoutViewModel.addDoWorkoutExerciseSet(exerciseData)
            }
        ) { exerciseDataSheetPaddingValues ->
            DoWorkoutScaffold(
                modifier = screenModifier,
                screenTitle = currentExercise.name,
                onExitWorkoutAction = onExitWorkoutAction,
                onNextExerciseAction = {

                    //Disable skip next exercise button when NextExerciseCountdownScreen is visible
                    if(!state.doWorkoutData.isBetweenExerciseCountdown) {
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
                        currentSet = currentSet
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
                    nextExercise = if (state.doWorkoutData.isNextExercise) nextExercise else currentExercise,
                    currentSetNumber = nextSetNumber,
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
    countdownNumberPadding: PaddingValues = PaddingValues(vertical = 12.dp, horizontal = 6.dp)
) {
    val alpha = 0.5f
    val motivationalQuote = MockupDataGenerator.generateMotivationalQuote()
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
                modifier = Modifier.padding(countdownNumberPadding),
                text = countdownTime.toSeconds().toString(),
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
    val countdownTimer = TimerUtil(countdownTime.toSeconds())
    NextExerciseCountdownScreen(
        nextExercise = nextExercise,
        currentSetNumber = currentSetNumber,
        countdownTime = countdownTime,
        defaultTotalSets = defaultTotalSets
    )
}

@Composable
fun HalfTransparentColumn(alpha: Float = 0.3f, content: @Composable ColumnScope.() -> Unit) {
    Column {
        content()

        Canvas(modifier = Modifier.fillMaxSize()) {
            val height = size.height / 2
            drawRect(
                color = Color.Black.copy(alpha = alpha),
                size = Size(size.width, height)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DoWorkoutFooterWithModal(
    totalTime: ExerciseTime,
    timeLeft: ExerciseTime,
    exercise: ExerciseDto,
    currentSetNumber: Int, //Used for CurrentExerciseInfoRow
    defaultTotalSets: Int, //Used for CurrentExerciseInfoRow
    isNextExercise: Boolean,
    onSaveExerciseData: (ExerciseProgressDto) -> Unit
) {
    val currentSet = exercise.sets[currentSetNumber - 1]

    ExerciseDataSheetModal2(
        exercise = exercise,
        currentSetNumber = currentSetNumber,
        defaultTotalSets = defaultTotalSets,
        isNextExercise = isNextExercise,
        onSaveExerciseData = onSaveExerciseData
    ) {

        //Footer
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.BottomCenter
        ) {
            DoWorkoutFooter(
                totalTime = totalTime,
                timeLeft = timeLeft,
                currentSet = currentSet
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun DoWorkoutFooterWithModalPreview() {
    val time = ExerciseTime(hours = 0, minutes = 1, seconds = 30)
    val timeLeft = ExerciseTime(hours = 0, minutes = 1, seconds = 15)
    val exercise = MockupDataGenerator.generateExercise()
    val currentSetNumber = 1
    val defaultTotalSets = 4
    val workoutTimer = TimerUtil(time.toSeconds())
    val onTimePassed: (ExerciseTime) -> Unit = {

    }
    val onSaveExerciseData: (ExerciseProgressDto) -> Unit = {

    }

    DoWorkoutFooterWithModal(
        totalTime = time,
        timeLeft = timeLeft,
        exercise = exercise,
        currentSetNumber = currentSetNumber,
        defaultTotalSets = defaultTotalSets,
        isNextExercise = false,
        onSaveExerciseData = onSaveExerciseData
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DoWorkoutFooter(
    exerciseTimerStyle: ExerciseTimerStyle = ExerciseTimerStyle(),
    totalTime: ExerciseTime,
    timeLeft: ExerciseTime,
    currentSet: ExerciseSetDto
) {
    val exerciseDataPadding = PaddingValues(4.dp)
    val textColor = Color.White

    val repsText = currentSet.reps.toString()
    val weightText = if (currentSet.weight == 0.0f) "--" else currentSet.weight.toString()
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
                text = repsText,
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
        ExerciseTimer(
            modifier = Modifier
                .fillMaxHeight()
                .weight(2.5f),
            timeLeft = timeLeft,
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
                text = weightText,
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
    val timeLeft = ExerciseTime(hours = 0, minutes = 1, seconds = 15)
    val currentSet = MockupDataGenerator.generateExerciseSet()
    val workoutTimer = TimerUtil(time.toSeconds())
    val onTimePassed: (ExerciseTime) -> Unit = {

    }
    DoWorkoutFooter(
        totalTime = time,
        timeLeft = timeLeft,
        currentSet = currentSet
    )
}


//When set list is empty there are no rows showing
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
    currentSetNumber: Int,
    defaultTotalSets: Int,
    isNextExercise: Boolean = false,
    onSaveExerciseData: (ExerciseProgressDto) -> Unit,
    content: @Composable (paddingValues: PaddingValues) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val sheetPeekHeight = 88.dp

    var updatedExerciseWithProgressSets by remember {
        mutableStateOf(
            ExerciseProgressDto(
                exerciseId = exercise.exerciseId,
                workoutId = exercise.workoutId,
                name = exercise.name,
                muscleGroup = exercise.muscleGroup,
                machineType = exercise.machineType,
                snapshot = exercise.snapshot,
                sets = emptyList(),
            )
        )
    }

    //Sets are inserted in exercise internally
    val onExerciseDataChange: (ExerciseProgressDto) -> Unit = { updatedExercise ->
        updatedExerciseWithProgressSets = updatedExercise
    }

    //Save data before changing the exercise in ExerciseDataSheet
    LaunchedEffect(isNextExercise) {
        if(isNextExercise){
            onSaveExerciseData(updatedExerciseWithProgressSets)
        }
    }

    val scaffoldState = rememberBottomSheetScaffoldState()
    BottomSheetScaffold(
        modifier = Modifier
            .fillMaxWidth(),
        scaffoldState = scaffoldState,
        sheetContent = {
            CurrentExerciseInfoRow(
                currentExercise = exercise,
                currentSetNumber = currentSetNumber,
                defaultTotalSets = defaultTotalSets
            )

            ExerciseDataSheet(exercise = exercise, onExerciseDataChange = onExerciseDataChange)
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
fun CurrentExerciseInfoRow(
    currentExercise: ExerciseDto,
    currentSetNumber: Int,
    defaultTotalSets: Int
) {
    val textColor = Color.White
    val setsTextColor = Color.Yellow
    val cornerSize = 24.dp

    val totalSets =
        if (currentExercise.sets.isNotEmpty()) currentExercise.sets.size else defaultTotalSets

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
            text = "$currentSetNumber of $totalSets",
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
    val currentSetNumber = 1
    val defaultTotalSets = 4
    CurrentExerciseInfoRow(
        currentExercise = currentExercise,
        currentSetNumber = currentSetNumber,
        defaultTotalSets = defaultTotalSets
    )
}

@Preview
@Composable
fun ExerciseDataSheetModal2Preview() {
    val exercise = MockupDataGenerator.generateExercise()
    val currentSetNumber = 1
    val defaultTotalSets = 4
    val onSaveExerciseData: (ExerciseProgressDto) -> Unit = {}
    ExerciseDataSheetModal2(
        exercise = exercise,
        currentSetNumber = currentSetNumber,
        defaultTotalSets = defaultTotalSets,
        isNextExercise = false,
        onSaveExerciseData = onSaveExerciseData
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
